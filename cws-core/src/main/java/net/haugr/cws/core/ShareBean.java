/*
 * CWS, Cryptographic Web Share - open source Cryptographic Sharing system.
 * Copyright (c) 2016-2022, haugr.net
 * mailto: cws AT haugr DOT net
 *
 * CWS is free software; you can redistribute it and/or modify it under the
 * terms of the Apache License, as published by the Apache Software Foundation.
 *
 * CWS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the Apache License for more details.
 *
 * You should have received a copy of the Apache License, version 2, along with
 * this program; If not, you can download a copy of the License
 * here: https://www.apache.org/licenses/
 */
package net.haugr.cws.core;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import net.haugr.cws.api.requests.FetchDataRequest;
import net.haugr.cws.api.requests.FetchDataTypeRequest;
import net.haugr.cws.api.requests.FetchSignatureRequest;
import net.haugr.cws.api.requests.ProcessDataRequest;
import net.haugr.cws.api.requests.ProcessDataTypeRequest;
import net.haugr.cws.api.requests.SignRequest;
import net.haugr.cws.api.requests.VerifyRequest;
import net.haugr.cws.api.responses.FetchDataResponse;
import net.haugr.cws.api.responses.FetchDataTypeResponse;
import net.haugr.cws.api.responses.FetchSignatureResponse;
import net.haugr.cws.api.responses.ProcessDataResponse;
import net.haugr.cws.api.responses.ProcessDataTypeResponse;
import net.haugr.cws.api.responses.SignResponse;
import net.haugr.cws.api.responses.VerifyResponse;
import net.haugr.cws.core.exceptions.CWSException;
import net.haugr.cws.core.managers.FetchDataManager;
import net.haugr.cws.core.managers.FetchDataTypeManager;
import net.haugr.cws.core.managers.FetchSignatureManager;
import net.haugr.cws.core.managers.ProcessDataManager;
import net.haugr.cws.core.managers.ProcessDataTypeManager;
import net.haugr.cws.core.managers.SignManager;
import net.haugr.cws.core.managers.VerifyManager;
import net.haugr.cws.core.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Java EE Bean for the Share functionality and final Error handling layer.
 * This is also the layer where transactions are controlled.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.0
 */
@Stateless
public class ShareBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareBean.class);

    @PersistenceContext
    private EntityManager entityManager;
    private final Settings settings = Settings.getInstance();

    @Transactional(Transactional.TxType.REQUIRED)
    public ProcessDataTypeResponse processDataType(final ProcessDataTypeRequest request) {
        ProcessDataTypeManager manager = null;
        ProcessDataTypeResponse response;

        try {
            manager = new ProcessDataTypeManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new ProcessDataTypeResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public FetchDataTypeResponse fetchDataTypes(final FetchDataTypeRequest request) {
        FetchDataTypeManager manager = null;
        FetchDataTypeResponse response;

        try {
            manager = new FetchDataTypeManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new FetchDataTypeResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public ProcessDataResponse processData(final ProcessDataRequest request) {
        ProcessDataManager manager = null;
        ProcessDataResponse response;

        try {
            manager = new ProcessDataManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new ProcessDataResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public FetchDataResponse fetchData(final FetchDataRequest request) {
        FetchDataManager manager = null;
        FetchDataResponse response;

        try {
            manager = new FetchDataManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new FetchDataResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public SignResponse sign(final SignRequest request) {
        SignManager manager = null;
        SignResponse response;

        try {
            manager = new SignManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new SignResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public VerifyResponse verify(final VerifyRequest request) {
        VerifyManager manager = null;
        VerifyResponse response;

        try {
            manager = new VerifyManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new VerifyResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public FetchSignatureResponse fetchSignatures(final FetchSignatureRequest request) {
        FetchSignatureManager manager = null;
        FetchSignatureResponse response;

        try {
            manager = new FetchSignatureManager(settings, entityManager);
            response = manager.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough
            // information, so it can be dealt with by the requesting
            // System. Logging the error is thus not needed, as all
            // information is provided in the response.
            LOGGER.debug(e.getMessage(), e);
            response = new FetchSignatureResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(manager);
        }

        return response;
    }
}
