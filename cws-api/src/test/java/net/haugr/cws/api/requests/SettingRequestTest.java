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
package net.haugr.cws.api.requests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.haugr.cws.api.TestUtilities;
import net.haugr.cws.api.common.Constants;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * @author Kim Jensen
 * @since CWS 1.0
 */
final class SettingRequestTest {

    @Test
    void testClassFlow() {
        final Map<String, String> settings = new HashMap<>();
        settings.put("Setting1", "Value1");

        final SettingRequest request = new SettingRequest();
        request.setAccountName(Constants.ADMIN_ACCOUNT);
        request.setCredential(TestUtilities.convert(Constants.ADMIN_ACCOUNT));
        assertTrue(request.getSettings().isEmpty());

        request.setSettings(settings);
        assertEquals(1, request.getSettings().size());
        assertEquals("Value1", request.getSettings().get("Setting1"));

        final Map<String, String> errors = request.validate();
        assertTrue(errors.isEmpty());
    }

    @Test
    void testEmptyClass() {
        final SettingRequest request = new SettingRequest();

        final Map<String, String> errors = request.validate();
        assertEquals(1, errors.size());
        assertEquals("The Session (Credential) is missing.", errors.get(Constants.FIELD_CREDENTIAL));
    }
}