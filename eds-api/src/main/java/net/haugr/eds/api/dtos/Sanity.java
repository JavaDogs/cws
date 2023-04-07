/*
 * EDS, Encrypted Data Share - open source Cryptographic Sharing system.
 * Copyright (c) 2016-2023, haugr.net
 * mailto: eds AT haugr DOT net
 *
 * EDS is free software; you can redistribute it and/or modify it under the
 * terms of the Apache License, as published by the Apache Software Foundation.
 *
 * EDS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the Apache License for more details.
 *
 * You should have received a copy of the Apache License, version 2, along with
 * this program; If not, you can download a copy of the License
 * here: https://www.apache.org/licenses/
 */
package net.haugr.eds.api.dtos;

import net.haugr.eds.api.common.Constants;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

/**
 * The Sanity Object contain information about a Data record, which has failed
 * the sanity check, i.e. the encrypted bytes have been changed, so it is no
 * longer possible to decrypt the Object. The information returned include the
 * ID of the Data Object which failed the sanity check, and the date at which it
 * failed it.
 *
 * @author Kim Jensen
 * @since EDS 1.0
 */
@JsonbPropertyOrder({ Constants.FIELD_DATA_ID, Constants.FIELD_CHANGED })
public final class Sanity implements Serializable {

    /** {@link Constants#SERIAL_VERSION_UID}. */
    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    /** DataId. */
    @JsonbProperty(value = Constants.FIELD_DATA_ID, nillable = true)
    private String dataId = null;

    /** Changed. */
    @JsonbProperty(value = Constants.FIELD_CHANGED, nillable = true)
    @JsonbDateFormat(Constants.JSON_DATE_FORMAT)
    private LocalDateTime changed = null;

    // =========================================================================
    // Standard Setters & Getters
    // =========================================================================

    /**
     * Set the DataId.
     *
     * @param dataId DataId
     */
    public void setDataId(final String dataId) {
        this.dataId = dataId;
    }

    /**
     * Retrieves the DataId.
     *
     * @return DataId
     */
    public String getDataId() {
        return dataId;
    }

    /**
     * Sets the Changed Timestamp.
     *
     * @param changed Changed Timestamp
     */
    public void setChanged(final LocalDateTime changed) {
        this.changed = changed;
    }

    /**
     * Retrieves the Changed Timestamp.
     *
     * @return Changed Timestamp
     */
    public LocalDateTime getChanged() {
        return changed;
    }

    // =========================================================================
    // Standard Methods
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Sanity{" +
                "dataId='" + dataId + '\'' +
                ", changed=" + changed +
                '}';
    }
}