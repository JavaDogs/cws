/*
 * =============================================================================
 * Copyright (c) 2016-2017, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-core)
 * =============================================================================
 */
package io.javadog.cws.core.services;

import io.javadog.cws.api.common.Constants;
import io.javadog.cws.api.common.ReturnCode;
import io.javadog.cws.api.requests.ProcessDataRequest;
import io.javadog.cws.api.responses.ProcessDataResponse;
import io.javadog.cws.common.Settings;
import io.javadog.cws.common.enums.KeyAlgorithm;
import io.javadog.cws.common.enums.SanityStatus;
import io.javadog.cws.common.exceptions.CWSException;
import io.javadog.cws.common.keys.SecretCWSKey;
import io.javadog.cws.core.Permission;
import io.javadog.cws.core.Serviceable;
import io.javadog.cws.model.entities.DataEntity;
import io.javadog.cws.model.entities.DataTypeEntity;
import io.javadog.cws.model.entities.KeyEntity;
import io.javadog.cws.model.entities.MetadataEntity;
import io.javadog.cws.model.entities.TrusteeEntity;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class ProcessDataService extends Serviceable<ProcessDataResponse, ProcessDataRequest> {

    public ProcessDataService(final Settings settings, final EntityManager entityManager) {
        super(settings, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessDataResponse perform(final ProcessDataRequest request) {
        verifyRequest(request, Permission.PROCESS_DATA);

        // Since the verification above is of a more general nature, it is
        // important that the processing is being double checked against the
        // actual Circle.
        final ProcessDataResponse response;

        switch (request.getAction()) {
            case ADD:
                response = processAddData(request);
                break;
            case UPDATE:
                response = processUpdateData(request);
                break;
            case DELETE:
                response = processDeleteData(request);
                break;
            default:
                // Unreachable Code by design.
                throw new CWSException(ReturnCode.ILLEGAL_ACTION, "Unsupported Action.");
        }

        return response;
    }

    private ProcessDataResponse processAddData(final ProcessDataRequest request) {
        final DataTypeEntity type = findDataType(request.getTypeName());
        final MetadataEntity parent = findParent(request);
        final MetadataEntity existingName = dao.findInFolder(member, parent, request.getDataName());
        final ProcessDataResponse response;

        if (existingName == null) {
            final TrusteeEntity trustee = findTrustee(request.getCircleId());
            final byte[] bytes = request.getData();

            if (Objects.equals(Constants.FOLDER_TYPENAME, type.getName())) {
                response = createFolder(trustee, request);
            } else if (bytes != null) {
                final MetadataEntity metadataEntity = createMetadataEntity(trustee, type, parent.getId(), request.getDataName());
                final KeyEntity keyEntity = trustee.getKey();
                final KeyAlgorithm algorithm = keyEntity.getAlgorithm();

                final SecretCWSKey circleKey = crypto.extractCircleKey(algorithm, keyPair.getPrivate(), trustee.getCircleKey());
                final String salt = UUID.randomUUID().toString();
                circleKey.setSalt(salt);

                final DataEntity dataEntity = new DataEntity();
                dataEntity.setMetadata(metadataEntity);
                dataEntity.setKey(keyEntity);
                dataEntity.setData(crypto.encrypt(circleKey, bytes));
                dataEntity.setInitialVector(salt);
                dataEntity.setChecksum(crypto.generateChecksum(dataEntity.getData()));
                dataEntity.setSanityStatus(SanityStatus.OK);
                dataEntity.setSanityChecked(new Date());

                dao.persist(dataEntity);
                response = new ProcessDataResponse();
                response.setDataId(metadataEntity.getExternalId());
            } else {
                final MetadataEntity metadataEntity = createMetadataEntity(trustee, type, parent.getId(), request.getDataName());
                response = new ProcessDataResponse();
                response.setDataId(metadataEntity.getExternalId());
            }
        } else {
            response = new ProcessDataResponse(ReturnCode.INTEGRITY_WARNING, "Another record with the same name already exists.");
        }

        return response;
    }

    private ProcessDataResponse processUpdateData(final ProcessDataRequest request) {
        final MetadataEntity entity = dao.findMetaDataByMemberAndExternalId(member, request.getDataId());
        final ProcessDataResponse response;

        if (entity != null) {
            // Now, check if the member account is allowed to perform the requested
            // action. If not allowed, then an Exception is thrown.
            findTrustee(entity.getCircle().getExternalId());

            // First, let's identify the folder, we're not updating it yet, only
            // after the name has also been checked.
            Long folderId = entity.getParentId();
            if (request.getFolderId() != null) {
                final MetadataEntity folder = checkFolder(entity, request.getFolderId());
                folderId = folder.getId();
            }

            entity.setName(checkName(entity, request.getDataName(), folderId));
            checkData(entity, request.getData());
            entity.setParentId(folderId);
            dao.persist(entity);

            response = new ProcessDataResponse();
            response.setDataId(entity.getExternalId());
        } else {
            response = new ProcessDataResponse(ReturnCode.IDENTIFICATION_WARNING, "The requested Data Object could not be found.");
        }

        return response;
    }

    private ProcessDataResponse processDeleteData(final ProcessDataRequest request) {
        final MetadataEntity entity = dao.findMetaDataByMemberAndExternalId(member, request.getDataId());
        final ProcessDataResponse response;

        if (entity != null) {
            // Now, check if the member account is allowed to perform the requested
            // action. If not allowed, then an Exception is thrown.
            findTrustee(entity.getCircle().getExternalId());

            if (Objects.equals(Constants.FOLDER_TYPENAME, entity.getType().getName())) {
                // If the Entity is a Folder, then we must check if it
                // currently has content, if so - then we cannot delete it.
                final long count = dao.countFolderContent(entity);
                if (count > 0) {
                    response = new ProcessDataResponse(ReturnCode.INTEGRITY_WARNING, "The requested Folder cannot be removed as it is not empty.");
                } else {
                    dao.delete(entity);
                    response = new ProcessDataResponse();
                }
            } else {
                dao.delete(entity);
                response = new ProcessDataResponse();
            }
        } else {
            response = new ProcessDataResponse(ReturnCode.IDENTIFICATION_WARNING, "The requested Data Object could not be found.");
        }

        return response;
    }

    private DataTypeEntity findDataType(final String typeName) {
        DataTypeEntity entity = null;

        if (typeName != null) {
            entity = dao.findDataTypeByName(typeName);
            if (entity == null) {
                throw new CWSException(ReturnCode.INTEGRITY_WARNING, "Cannot find a matching DataType for the Object.");
            }
        }

        if (entity == null) {
            entity = dao.findDataTypeByName(Constants.DATA_TYPENAME);
        }

        return entity;
    }

    private MetadataEntity createMetadataEntity(final TrusteeEntity trustee, final DataTypeEntity type, final Long parentId, final String name) {
        final MetadataEntity entity = new MetadataEntity();
        entity.setCircle(trustee.getCircle());
        entity.setName(name);
        entity.setParentId(parentId);
        entity.setType(type);
        dao.persist(entity);

        return entity;
    }

    private MetadataEntity findParent(final ProcessDataRequest request) {
        final MetadataEntity entity;

        if (request.getFolderId() != null) {
            entity = dao.findMetaDataByMemberAndExternalId(member, request.getFolderId());
            if ((entity == null) || !Objects.equals(Constants.FOLDER_TYPENAME, entity.getType().getName())) {
                throw new CWSException(ReturnCode.IDENTIFICATION_WARNING, "Provided FolderId is not a folder.");
            }
        } else {
            entity = dao.findRootByMemberCircle(member, request.getCircleId());
        }

        return entity;
    }

    private ProcessDataResponse createFolder(final TrusteeEntity trustee, final ProcessDataRequest request) {
        final MetadataEntity parent = findParent(request);
        final DataTypeEntity folderType = dao.findDataTypeByName(Constants.FOLDER_TYPENAME);
        final MetadataEntity folder = createMetadata(trustee, request.getDataName(), parent.getId(), folderType);

        final ProcessDataResponse response = new ProcessDataResponse();
        response.setDataId(folder.getExternalId());

        return response;
    }

    private MetadataEntity createMetadata(final TrusteeEntity trustee, final String name, final Long parentId, final DataTypeEntity dataType) {
        final MetadataEntity entity = new MetadataEntity();
        entity.setCircle(trustee.getCircle());
        entity.setName(name);
        entity.setParentId(parentId);
        entity.setType(dataType);
        dao.persist(entity);

        return entity;
    }

    /**
     * <p>It is possible to move Data from one Folder to another, but it is not
     * permitted to move a Folder, as this is fairly problematic due to the
     * restraints in the Data Model, which has been added to prevent looping
     * models.</p>
     * <p>
     * <p>For the same reason, the FolderId must internally remain a number, so
     * it can be checked for consistency - although the externally exposed Id
     * is the UUID or ExternalId value.</p>
     *
     * @param entity   Data Entity to check the Folder of
     * @param folderId The External Id of the Folder to check
     * @return Metadata Entity of the Folder
     */
    private MetadataEntity checkFolder(final MetadataEntity entity, final String folderId) {
        final MetadataEntity folder;

        folder = dao.findMetaDataByMemberAndExternalId(member, folderId);
        if (folder != null) {
            final Long currentCircleId = entity.getCircle().getId();
            final Long foundCircleId = folder.getCircle().getId();

            if (Objects.equals(currentCircleId, foundCircleId)) {
                if (Objects.equals(Constants.FOLDER_TYPENAME, entity.getType().getName())) {
                    throw new CWSException(ReturnCode.ILLEGAL_ACTION, "It is not permitted to move Folders.");
                }
            } else {
                throw new CWSException(ReturnCode.ILLEGAL_ACTION, "Moving Data from one Circle to another is not permitted.");
            }
        } else {
            throw new CWSException(ReturnCode.INTEGRITY_WARNING, "No existing Folder could be found.");
        }

        return folder;
    }

    private String checkName(final MetadataEntity entity, final String name, final Long folderId) {
        final String theName = (name != null) ? name.trim() : entity.getName();

        if (dao.checkIfNameIsUsed(entity, theName, folderId)) {
            throw new CWSException(ReturnCode.IDENTIFICATION_WARNING, "The name provided is already being used in the given folder.");
        }

        return theName;
    }

    private void checkData(final MetadataEntity metadata, final byte[] bytes) {
        if (bytes != null) {
            final TrusteeEntity trustee = findTrustee(metadata.getCircle().getExternalId());

            DataEntity entity = dao.findDataByMetadata(metadata);
            if (entity == null) {
                entity = new DataEntity();
                entity.setMetadata(metadata);
                entity.setKey(trustee.getKey());
            }

            final SecretCWSKey circleKey = extractCircleKey(entity);
            final String salt = UUID.randomUUID().toString();
            circleKey.setSalt(salt);
            final byte[] encrypted = crypto.encrypt(circleKey, bytes);
            final String checksum = crypto.generateChecksum(encrypted);

            entity.setData(encrypted);
            entity.setInitialVector(salt);
            entity.setChecksum(checksum);
            entity.setSanityStatus(SanityStatus.OK);
            entity.setSanityChecked(new Date());
            dao.persist(entity);
        }
    }
}
