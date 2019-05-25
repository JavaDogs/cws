/*
 * CWS, Cryptographic Web Store - open source Cryptographic Storage system.
 * Copyright (C) 2016-2019, JavaDog.io
 * mailto: cws AT JavaDog DOT io
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
package io.javadog.cws.core.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.javadog.cws.core.DatabaseSetup;
import io.javadog.cws.core.enums.HashAlgorithm;
import io.javadog.cws.core.enums.KeyAlgorithm;
import io.javadog.cws.core.enums.StandardSetting;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class SettingsTest extends DatabaseSetup {

    @Test
    public void testSettings() {
        final Settings mySettings = newSettings();

        final Map<String, String> existing = mySettings.get();
        assertThat(existing.size(), is(StandardSetting.values().length));
        mySettings.set("my.new.key", "the awesome value");

        final Map<String, String> updated = mySettings.get();
        assertThat(updated.size(), is(existing.size() + 1));
    }

    @Test
    public void testReadingDefaultSettings() {
        final Settings mySettings = newSettings();

        final Set<KeyAlgorithm> aes = prepareSetOf(KeyAlgorithm.Type.SYMMETRIC);
        final Set<KeyAlgorithm> rsa = prepareSetOf(KeyAlgorithm.Type.ASYMMETRIC);
        final Set<KeyAlgorithm> sha = prepareSetOf(KeyAlgorithm.Type.SIGNATURE);
        final Set<KeyAlgorithm> pbe = prepareSetOf(KeyAlgorithm.Type.PASSWORD);

        assertTrue(aes.contains(mySettings.getSymmetricAlgorithm()));
        assertTrue(rsa.contains(mySettings.getAsymmetricAlgorithm()));
        assertTrue(sha.contains(mySettings.getSignatureAlgorithm()));
        assertTrue(pbe.contains(mySettings.getPasswordAlgorithm()));
        assertThat(mySettings.getSalt(), is("Default salt, also used as kill switch. Must be set in DB."));
        assertThat(mySettings.getCharset().name(), is("UTF-8"));
    }

    @Test
    public void testUpdateDefaultSettings() {
        final Settings mySettings = newSettings();

        mySettings.set(StandardSetting.SYMMETRIC_ALGORITHM, KeyAlgorithm.AES_CBC_192.name());
        assertThat(mySettings.getSymmetricAlgorithm(), is(KeyAlgorithm.AES_CBC_192));

        mySettings.set(StandardSetting.ASYMMETRIC_ALGORITHM, KeyAlgorithm.RSA_4096.name());
        assertThat(mySettings.getAsymmetricAlgorithm(), is(KeyAlgorithm.RSA_4096));

        mySettings.set(StandardSetting.ASYMMETRIC_ALGORITHM, KeyAlgorithm.RSA_8192.name());
        assertThat(mySettings.getAsymmetricAlgorithm(), is(KeyAlgorithm.RSA_8192));

        mySettings.set(StandardSetting.SIGNATURE_ALGORITHM, KeyAlgorithm.SHA_256.name());
        assertThat(mySettings.getSignatureAlgorithm(), is(KeyAlgorithm.SHA_256));

        mySettings.set(StandardSetting.HASH_ALGORITHM, HashAlgorithm.SHA_256.name());
        assertThat(mySettings.getHashAlgorithm(), is(HashAlgorithm.SHA_256));

        mySettings.set(StandardSetting.PBE_ALGORITHM, KeyAlgorithm.PBE_192.name());
        assertThat(mySettings.getPasswordAlgorithm(), is(KeyAlgorithm.PBE_192));

        mySettings.set(StandardSetting.PBE_ALGORITHM, KeyAlgorithm.PBE_256.name());
        assertThat(mySettings.getPasswordAlgorithm(), is(KeyAlgorithm.PBE_256));

        mySettings.set(StandardSetting.CWS_SALT, "UUID value");
        assertThat(mySettings.getSalt(), is("UUID value"));

        mySettings.set(StandardSetting.CWS_LOCALE, Locale.GERMAN.toString());
        assertThat(mySettings.getLocale(), is(Locale.GERMAN));

        mySettings.set(StandardSetting.CWS_CHARSET, "ISO-8859-15");
        assertThat(mySettings.getCharset().name(), is("ISO-8859-15"));

        mySettings.set(StandardSetting.SANITY_STARTUP, "false");
        assertFalse(mySettings.getSanityStartup());

        mySettings.set(StandardSetting.SANITY_INTERVAL, "120");
        assertThat(mySettings.getSanityInterval(), is(120));

        mySettings.set(StandardSetting.SESSION_TIMEOUT, "240");
        assertThat(mySettings.getSessionTimeout(), is(240));
    }

    @Test
    public void testShowOtherMemberInformation() {
        final Settings mySettings = newSettings();
        assertTrue(mySettings.getShareTrustees());
        mySettings.set(StandardSetting.SHOW_TRUSTEES, "false");
        assertFalse(mySettings.getShareTrustees());
        mySettings.set(StandardSetting.SHOW_TRUSTEES, "true");
        assertTrue(mySettings.getShareTrustees());
        mySettings.set(StandardSetting.SHOW_TRUSTEES, " true ");
        assertTrue(mySettings.getShareTrustees());
        mySettings.set(StandardSetting.SHOW_TRUSTEES, "");
        assertFalse(mySettings.getShareTrustees());
        mySettings.set(StandardSetting.SHOW_TRUSTEES, " what ");
        assertFalse(mySettings.getShareTrustees());
    }

    // =========================================================================
    // Internal methods
    // =========================================================================

    private static Set<KeyAlgorithm> prepareSetOf(final KeyAlgorithm.Type type) {
        final Set<KeyAlgorithm> set = EnumSet.noneOf(KeyAlgorithm.class);

        for (final KeyAlgorithm algorithm : KeyAlgorithm.values()) {
            if (algorithm.getType() == type) {
                set.add(algorithm);
            }
        }

        return set;
    }
}
