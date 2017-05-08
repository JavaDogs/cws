/*
 * =============================================================================
 * Copyright (c) 2016-2017, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-api)
 * =============================================================================
 */
package io.javadog.cws.api.dtos;

import io.javadog.cws.api.common.Constants;
import io.javadog.cws.api.common.Verifiable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "objectData", propOrder = { "id", "name", "type", "bytes", "added" })
public final class Data extends Verifiable {

    /** {@link Constants#SERIAL_VERSION_UID}. */
    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    private static final String FIELD_ID = "id";
    private static final String FIELD_FOLDER_ID = "folderId";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_BYTES = "bytes";
    private static final String FIELD_ADDED = "added";

    @XmlElement(name = FIELD_ID, required = true, nillable = true)
    private String id = null;

    @XmlElement(name = FIELD_FOLDER_ID, required = true, nillable = true)
    private String folderId = null;

    @XmlElement(name = FIELD_NAME, required = true, nillable = true)
    private String name = null;

    @XmlElement(name = FIELD_TYPE, required = true)
    private DataType type = null;

    @XmlElement(name = FIELD_BYTES, required = true, nillable = true)
    private byte[] bytes = null;

    @XmlElement(name = FIELD_ADDED)
    private Date added = null;

    // =========================================================================
    // Standard Setters & Getters
    // =========================================================================

    @Pattern(regexp = Constants.ID_PATTERN_REGEX)
    public void setId(final String id) {
        ensurePattern(FIELD_ID, id, Constants.ID_PATTERN_REGEX);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Pattern(regexp = Constants.ID_PATTERN_REGEX)
    public void setFolderId(final String folderId) {
        ensurePattern(FIELD_FOLDER_ID, folderId, Constants.ID_PATTERN_REGEX);
        this.folderId = folderId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NotNull
    public void setType(final DataType objectType) {
        ensureNotNull(FIELD_TYPE, objectType);
        ensureVerifiable(FIELD_TYPE, objectType);
        this.type = objectType;
    }

    public DataType getType() {
        return type;
    }

    public void setAdded(final Date added) {
        this.added = new Date(added.getTime());
    }

    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Date getAdded() {
        return new Date(added.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> validate() {
        final Map<String, String> errors = new HashMap<>();

        checkPattern(errors, FIELD_ID, id, Constants.ID_PATTERN_REGEX, "The Object Data Id is invalid.");
        checkPattern(errors, FIELD_FOLDER_ID, folderId, Constants.ID_PATTERN_REGEX, "The Object Folder Id is invalid.");
        checkNotNull(errors, FIELD_TYPE, type, "The ObjectType is undefined.");
        if (type != null) {
            errors.putAll(type.validate());
        }

        return errors;
    }
}
