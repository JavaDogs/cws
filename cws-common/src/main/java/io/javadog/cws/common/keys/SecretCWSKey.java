/*
 * =============================================================================
 * Copyright (c) 2016-2017, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-common)
 * =============================================================================
 */
package io.javadog.cws.common.keys;

import io.javadog.cws.common.enums.KeyAlgorithm;

import javax.crypto.SecretKey;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class SecretCWSKey extends CWSKey<SecretKey> {

    private String salt = null;

    /**
     * Default Constructor.
     *
     * @param algorithm Key Algorithm
     * @param key       Key
     */
    public SecretCWSKey(final KeyAlgorithm algorithm, final SecretKey key) {
        super(algorithm, key);
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecretKey getKey() {
        return key;
    }

    public void destroy() {
        if (!destroyed) {
            // Updating the flag first, so any further attempts at
            // destroying the Key will be ignored.
            destroyed = true;

            // Theoretically, we should just invoke the key.destroy() here,
            // but each invocation of fails with a DestroyFailedException!
        }
    }
}
