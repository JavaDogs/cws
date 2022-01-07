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
package net.haugr.cws.core.model;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.haugr.cws.api.common.ReturnCode;
import net.haugr.cws.core.enums.HashAlgorithm;
import net.haugr.cws.core.enums.KeyAlgorithm;
import net.haugr.cws.core.enums.StandardSetting;
import net.haugr.cws.core.exceptions.CWSException;

/**
 * <p>This Class holds the general settings for the CWS. All settings used by
 * the CWS is set with default values, which may or may nor be overwritten,
 * depending on the underlying database. It is possible to extend the settings
 * with more values, if Client Systems needs certain information shared.</p>
 *
 * <p>By starting the CWS, the Settings are loaded from the Database, and
 * invoking the Settings Request will allow the System Administrator to change
 * the Settings. It is not possible for Circle Administrators to make any
 * Updates, since this may have impacts on the System.</p>
 *
 * <p>Once a CWS system is being actively used, i.e. there exist Member Accounts
 * other than the System Administrator, then the rules regarding the
 * non-updatable values will be enforced. Updatable fields may be changed, but
 * the CWS will only use them after a restart.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.0
 */
public final class Settings {

    private static Settings instance = null;
    private static final Object LOCK = new Object();

    private final Properties properties = new Properties();

    /**
     * Private Constructor, this is a Singleton.
     */
    private Settings() {
        for (final StandardSetting setting : StandardSetting.values()) {
            set(setting, setting.getValue());
        }
    }

    public static Settings getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new Settings();
            }

            return instance;
        }
    }

    // =========================================================================
    // Generic Settings Methods
    // =========================================================================

    public void set(final StandardSetting key, final String value) {
        set(key.getKey(), value);
    }

    public void set(final String key, final String value) {
        properties.setProperty(key, value);
    }

    public String get(final String key) {
        return properties.getProperty(key);
    }

    public Map<String, String> get() {
        final Map<String, String> copy = new ConcurrentHashMap<>(16);

        for (final String key : keys()) {
            copy.put(key, get(key));
        }

        return copy;
    }

    public Set<String> keys() {
        return properties.stringPropertyNames();
    }

    public void remove(final String key) {
        properties.remove(key);
    }

    // =========================================================================
    // Specific Setting Getter Methods
    // =========================================================================

    public KeyAlgorithm getSymmetricAlgorithm() {
        return KeyAlgorithm.valueOf(get(StandardSetting.SYMMETRIC_ALGORITHM.getKey()));
    }

    public KeyAlgorithm getAsymmetricAlgorithm() {
        return KeyAlgorithm.valueOf(get(StandardSetting.ASYMMETRIC_ALGORITHM.getKey()));
    }

    public KeyAlgorithm getSignatureAlgorithm() {
        return KeyAlgorithm.valueOf(get(StandardSetting.SIGNATURE_ALGORITHM.getKey()));
    }

    public KeyAlgorithm getPasswordAlgorithm() {
        return KeyAlgorithm.valueOf(get(StandardSetting.PBE_ALGORITHM.getKey()));
    }

    public Integer getPasswordIterations() {
        return Integer.valueOf(get(StandardSetting.PBE_ITERATIONS.getKey()).trim());
    }

    public HashAlgorithm getHashAlgorithm() {
        return HashAlgorithm.valueOf(get(StandardSetting.HASH_ALGORITHM.getKey()));
    }

    public String getSalt() {
        return get(StandardSetting.CWS_SALT.getKey());
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(get(StandardSetting.CWS_LOCALE.getKey()));
    }

    public Charset getCharset() {
        try {
            return Charset.forName(get(StandardSetting.CWS_CHARSET.getKey()));
        } catch (IllegalArgumentException e) {
            throw new CWSException(ReturnCode.SETTING_ERROR, e);
        }
    }

    public boolean hasShowAllCircles() {
        return Boolean.parseBoolean(get(StandardSetting.SHOW_CIRCLES.getKey()).trim());
    }

    public boolean hasShareTrustees() {
        return Boolean.parseBoolean(get(StandardSetting.SHOW_TRUSTEES.getKey()).trim());
    }

    public boolean hasSanityStartup() {
        return Boolean.parseBoolean(get(StandardSetting.SANITY_STARTUP.getKey()).trim());
    }

    public Integer getSanityInterval() {
        return Integer.valueOf(get(StandardSetting.SANITY_INTERVAL.getKey()).trim());
    }

    public Integer getSessionTimeout() {
        return Integer.valueOf(get(StandardSetting.SESSION_TIMEOUT.getKey()).trim());
    }

    public String getMasterKeyURL() {
        return get(StandardSetting.MASTERKEY_URL.getKey()).trim();
    }

    public String getCORS() {
        return get(StandardSetting.CORS.getKey()).trim();
    }

    public boolean isReady() {
        return Boolean.parseBoolean(get(StandardSetting.IS_READY.getKey()).trim());
    }
}