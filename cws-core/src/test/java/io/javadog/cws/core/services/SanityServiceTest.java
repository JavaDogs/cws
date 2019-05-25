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
package io.javadog.cws.core.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.javadog.cws.api.common.Constants;
import io.javadog.cws.api.common.ReturnCode;
import io.javadog.cws.api.requests.FetchDataRequest;
import io.javadog.cws.api.requests.ProcessDataRequest;
import io.javadog.cws.api.requests.SanityRequest;
import io.javadog.cws.api.responses.FetchDataResponse;
import io.javadog.cws.api.responses.ProcessDataResponse;
import io.javadog.cws.api.responses.SanityResponse;
import io.javadog.cws.core.DatabaseSetup;
import io.javadog.cws.core.enums.SanityStatus;
import java.util.Date;
import org.junit.Test;

/**
 * @author Kim Jensen
 * @since CWS 1.0
 */
public final class SanityServiceTest extends DatabaseSetup {

    @Test
    public void testRequestAsSystemAdministrator() {
        prepareInvalidData();
        final SanityRequest request = prepareRequest(SanityRequest.class, Constants.ADMIN_ACCOUNT);
        final SanityService service = new SanityService(settings, entityManager);
        final SanityResponse response = service.perform(request);
        assertTrue(response.isOk());
        assertThat(response.getSanities().size(), is(6));
    }

    @Test
    public void testRequestAsSystemAdministratorForCircle() {
        prepareInvalidData();
        final SanityRequest request = prepareRequest(SanityRequest.class, Constants.ADMIN_ACCOUNT);
        request.setCircleId(CIRCLE_3_ID);
        request.setSince(new Date(10000L));
        final SanityService service = new SanityService(settings, entityManager);
        final SanityResponse response = service.perform(request);
        assertTrue(response.isOk());
        assertThat(response.getSanities().size(), is(1));
    }

    @Test
    public void testRequestAsCircleAdministratorForCircle() {
        prepareInvalidData();
        final SanityRequest request = prepareRequest(SanityRequest.class, MEMBER_1);
        final SanityService service = new SanityService(settings, entityManager);
        final SanityResponse response = service.perform(request);
        assertTrue(response.isOk());
        assertThat(response.getSanities().size(), is(4));
    }

    @Test
    public void testRequestAsCircleAdministratorForOtherCircle() {
        prepareCause(ReturnCode.IDENTIFICATION_WARNING, "No Trustee information found for member '" + MEMBER_1 + "' and circle '" + CIRCLE_3_ID + "'.");
        final SanityRequest request = prepareRequest(SanityRequest.class, MEMBER_1);
        request.setCircleId(CIRCLE_3_ID);
        final SanityService service = new SanityService(settings, entityManager);
        final SanityResponse response = service.perform(request);
        assertTrue(response.isOk());
        assertTrue(response.getSanities().isEmpty());
    }

    @Test
    public void testRequestAsMember() {
        prepareCause(ReturnCode.AUTHORIZATION_WARNING, "The requesting Account is not permitted to Process last Sanity Check.");

        final SanityService service = new SanityService(settings, entityManager);
        final SanityRequest request = prepareRequest(SanityRequest.class, MEMBER_5);
        assertTrue(request.validate().isEmpty());

        service.perform(request);
    }

    @Test
    public void testWithFailedChecksums() {
        final ProcessDataService dataService = new ProcessDataService(settings, entityManager);
        final ProcessDataRequest dataRequest = prepareAddDataRequest(MEMBER_1, CIRCLE_1_ID, "The Data", 524288);

        final ProcessDataResponse response = dataService.perform(dataRequest);
        assertTrue(response.isOk());
        falsifyChecksum(response, new Date(), SanityStatus.FAILED);

        // Now to the actual test - reading the data with invalid checksum
        final FetchDataService readDataService = new FetchDataService(settings, entityManager);
        final FetchDataRequest readDattaRequest = prepareReadRequest(response.getDataId());
        final FetchDataResponse readDataResponse = readDataService.perform(readDattaRequest);
        assertThat(readDataResponse.getReturnCode(), is(ReturnCode.INTEGRITY_ERROR.getCode()));
        assertThat(readDataResponse.getReturnMessage(), is("The Encrypted Data Checksum is invalid, the data appears to have been corrupted."));

        // Now for the actual testing...
        final SanityRequest sanityRequest = prepareRequest(SanityRequest.class, Constants.ADMIN_ACCOUNT);
        final SanityService sanityService = new SanityService(settings, entityManager);
        final SanityResponse sanityResponse = sanityService.perform(sanityRequest);
        assertTrue(sanityResponse.isOk());
        assertThat(sanityResponse.getSanities().size(), is(1));
        assertThat(sanityResponse.getSanities().get(0).getDataId(), is(response.getDataId()));
    }

    // =========================================================================
    // Internal Helper Methods
    // =========================================================================

    private void prepareInvalidData() {
        final ProcessDataService service = new ProcessDataService(settings, entityManager);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_1, CIRCLE_1_ID, "Invalidated Data1", 524288)), new Date(10L), SanityStatus.FAILED);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_1, CIRCLE_1_ID, "Invalidated Data2", 1048576)), new Date(), SanityStatus.FAILED);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_1, CIRCLE_2_ID, "Invalidated Data3", 524288)), new Date(10L), SanityStatus.FAILED);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_1, CIRCLE_2_ID, "Invalidated Data4", 1048576)), new Date(), SanityStatus.FAILED);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_4, CIRCLE_3_ID, "Invalidated Data5", 524288)), new Date(10L), SanityStatus.FAILED);
        falsifyChecksum(service.perform(prepareAddDataRequest(MEMBER_4, CIRCLE_3_ID, "Invalidated Data6", 1048576)), new Date(), SanityStatus.FAILED);
    }

    private static FetchDataRequest prepareReadRequest(final String dataId) {
        final FetchDataRequest request = prepareRequest(FetchDataRequest.class, MEMBER_1);
        request.setCircleId(CIRCLE_1_ID);
        request.setDataId(dataId);

        return request;
    }
}
