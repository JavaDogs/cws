/*
 * =============================================================================
 * Copyright (c) 2016-2018, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-api)
 * =============================================================================
 */
package io.javadog.cws.api.requests;

import io.javadog.cws.api.common.Constants;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This request Object may only be used by the System Administrator, to alter
 * or add/delete custom settings. Existing settings cannot be deleted, and some
 * may only be altered before Members exists. This is to prevent that changing
 * of values that is used to derive the internal Keys can still be properly
 * extracted.</p>
 *
 * <p>The Settings field is mandatory, but can be left either empty.</p>
 *
 * <p>Please see {@link Authentication} for information about the account and
 * credentials information.</p>
 *
 * @author Kim Jensen
 * @since  CWS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "settingRequest")
@XmlType(name = "settingRequest", propOrder = Constants.FIELD_SETTINGS)
public final class SettingRequest extends Authentication {

    /** {@link Constants#SERIAL_VERSION_UID}. */
    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    @NotNull
    @XmlElement(name = Constants.FIELD_SETTINGS, required = true)
    private HashMap<String, String> settings = null;

    // =========================================================================
    // Standard Setters & Getters
    // =========================================================================

    public void setSettings(final Map<String, String> settings) {
        this.settings = new HashMap<>(settings);
    }

    public Map<String, String> getSettings() {
        return (settings == null) ? new HashMap<>(0) : new HashMap<>(settings);
    }
}
