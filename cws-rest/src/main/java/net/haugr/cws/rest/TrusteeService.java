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
package net.haugr.cws.rest;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import net.haugr.cws.api.common.Action;
import net.haugr.cws.api.common.Constants;
import net.haugr.cws.api.common.ReturnCode;
import net.haugr.cws.api.requests.FetchTrusteeRequest;
import net.haugr.cws.api.requests.ProcessTrusteeRequest;
import net.haugr.cws.api.responses.FetchTrusteeResponse;
import net.haugr.cws.api.responses.ProcessTrusteeResponse;
import net.haugr.cws.core.ManagementBean;
import net.haugr.cws.core.misc.LoggingUtil;
import net.haugr.cws.core.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>REST interface for the Trustee functionality.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.0
 */
@Path(Constants.REST_TRUSTEES_BASE)
public class TrusteeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrusteeService.class);

    @Inject
    private ManagementBean bean;
    private final Settings settings = Settings.getInstance();

    @POST
    @Path(Constants.REST_TRUSTEES_ADD)
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    public Response add(@NotNull final ProcessTrusteeRequest addTrusteeRequest) {
        return processTrustee(addTrusteeRequest, Action.ADD, Constants.REST_TRUSTEES_ADD);
    }

    @POST
    @Path(Constants.REST_TRUSTEES_ALTER)
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    public Response alter(@NotNull final ProcessTrusteeRequest alterTrusteeRequest) {
        return processTrustee(alterTrusteeRequest, Action.ALTER, Constants.REST_TRUSTEES_ALTER);
    }

    @POST
    @Path(Constants.REST_TRUSTEES_REMOVE)
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    public Response remove(@NotNull final ProcessTrusteeRequest removeTrusteeRequest) {
        return processTrustee(removeTrusteeRequest, Action.REMOVE, Constants.REST_TRUSTEES_REMOVE);
    }

    @POST
    @Path(Constants.REST_TRUSTEES_FETCH)
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    public Response fetch(@NotNull final FetchTrusteeRequest fetchTrusteeRequest) {
        final String restAction = Constants.REST_TRUSTEES_BASE + Constants.REST_TRUSTEES_FETCH;
        final long startTime = System.nanoTime();
        FetchTrusteeResponse response;

        try {
            response = bean.fetchTrustees(fetchTrusteeRequest);
            LOGGER.info(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime));
        } catch (RuntimeException e) {
            LOGGER.error(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime, e), e);
            response = new FetchTrusteeResponse(ReturnCode.ERROR, e.getMessage());
        }

        return RestUtils.buildResponse(response);
    }

    private Response processTrustee(final ProcessTrusteeRequest request, final Action action, final String logAction) {
        final String restAction = Constants.REST_TRUSTEES_BASE + logAction;
        final long startTime = System.nanoTime();
        ProcessTrusteeResponse response;

        try {
            request.setAction(action);
            response = bean.processTrustee(request);
            LOGGER.info(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime));
        } catch (RuntimeException e) {
            LOGGER.error(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime, e), e);
            response = new ProcessTrusteeResponse(ReturnCode.ERROR, e.getMessage());
        }

        return RestUtils.buildResponse(response);
    }
}