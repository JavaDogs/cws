package io.javadog.cws.common;

import io.javadog.cws.common.exceptions.SettingException;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class SettingsTest {

    @Test
    public void testSettings() {
        final Settings settings = new Settings();

        final Map<String, String> existing = settings.get();
        assertThat(existing.size(), is(9));
        settings.set("my.new.key", "the awesome value");

        final Map<String, String> updated = settings.get();
        assertThat(updated.size(), is(existing.size() + 1));
    }

    @Test
    public void testReadingDefaultSettings() {
        final Settings settings = new Settings();

        assertThat(settings.getSymmetricAlgorithm(), is("AES/CBC/PKCS5Padding"));
        assertThat(settings.getSymmetricAlgorithmName(), is("AES"));
        assertThat(settings.getSymmetricCipherMode(), is("CBC"));
        assertThat(settings.getSymmetricKeylength(), is(128));
        assertThat(settings.getAsymmetricAlgorithmName(), is("RSA"));
        assertThat(settings.getAsymmetricKeylength(), is(2048));
        assertThat(settings.getPBEAlgorithm(), is("PBKDF2WithHmacSHA256"));
        assertThat(settings.getSalt(), is("Default Salt, please make sure it is set in the DB instead."));
        assertThat(settings.getCharset(), is("UTF-8"));
    }

    @Test
    public void testUpdateDefaultSettings() {
        final Settings settings = new Settings();

        settings.set(Settings.SYMMETRIC_ALGORITHM_NAME, "DES");
        settings.set(Settings.SYMMETRIC_ALGORITHM_MODE, "ECB");
        settings.set(Settings.SYMMETRIC_ALGORITHM_PADDING, "NoPadding");
        assertThat(settings.getSymmetricAlgorithm(), is("DES/ECB/NoPadding"));

        settings.set(Settings.SYMMETRIC_ALGORITHM_KEYLENGTH, "256");
        assertThat(settings.getSymmetricKeylength(), is(256));

        settings.set(Settings.ASYMMETRIC_ALGORITHM, "DSA");
        assertThat(settings.getAsymmetricAlgorithmName(), is("DSA"));

        settings.set(Settings.ASYMMETRIC_ALGORITHM_KEYLENGTH, "4096");
        assertThat(settings.getAsymmetricKeylength(), is(4096));

        settings.set(Settings.PBE_ALGORITHM, "PBEWithMD5AndTripleDES");
        assertThat(settings.getPBEAlgorithm(), is("PBEWithMD5AndTripleDES"));

        settings.set(Settings.CWS_SALT, "UUID value");
        assertThat(settings.getSalt(), is("UUID value"));

        settings.set(Settings.CWS_CHARSET, "ISO-8859-15");
        assertThat(settings.getCharset(), is("ISO-8859-15"));
    }

    @Test(expected = SettingException.class)
    public void setInvalidKeylength() {
        final Settings settings = new Settings();
        settings.set(Settings.ASYMMETRIC_ALGORITHM_KEYLENGTH, "Long");
        settings.getAsymmetricKeylength();
    }
}