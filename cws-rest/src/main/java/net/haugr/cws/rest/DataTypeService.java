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
import net.haugr.cws.api.requests.FetchDataTypeRequest;
import net.haugr.cws.api.requests.ProcessDataTypeRequest;
import net.haugr.cws.api.responses.FetchDataTypeResponse;
import net.haugr.cws.api.responses.ProcessDataTypeResponse;
import net.haugr.cws.core.ShareBean;
import net.haugr.cws.core.misc.LoggingUtil;
import net.haugr.cws.core.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>REST interface for the DataType functionality.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.0
 */
@Path(Constants.REST_DATATYPES_BASE)
public class DataTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeService.class);

    @Inject
    private ShareBean bean;
    private final Settings settings = Settings.getInstance();

    @POST
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    @Path(Constants.REST_DATATYPES_PROCESS)
    public Response process(@NotNull final ProcessDataTypeRequest processDataTypeRequest) {
        return processDataType(processDataTypeRequest, Action.PROCESS, Constants.REST_DATATYPES_PROCESS);
    }

    @POST
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    @Path(Constants.REST_DATATYPES_DELETE)
    public Response delete(@NotNull final ProcessDataTypeRequest deleteDataTypeRequest) {
        return processDataType(deleteDataTypeRequest, Action.DELETE, Constants.REST_DATATYPES_DELETE);
    }

    @POST
    @Consumes(RestUtils.CONSUMES)
    @Produces(RestUtils.PRODUCES)
    @Path(Constants.REST_DATATYPES_FETCH)
    public Response fetch(@NotNull final FetchDataTypeRequest fetchDataTypesRequest) {
        final String restAction = Constants.REST_DATATYPES_BASE + Constants.REST_DATATYPES_FETCH;
        final long startTime = System.nanoTime();
        FetchDataTypeResponse response;

        try {
            response = bean.fetchDataTypes(fetchDataTypesRequest);
            LOGGER.info(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime));
        } catch (RuntimeException e) {
            LOGGER.error(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime, e), e);
            response = new FetchDataTypeResponse(ReturnCode.ERROR, e.getMessage());
        }

        return RestUtils.buildResponse(response);
    }

    private Response processDataType(final ProcessDataTypeRequest request, final Action action, final String logAction) {
        final String restAction = Constants.REST_DATATYPES_BASE + logAction;
        final long startTime = System.nanoTime();
        ProcessDataTypeResponse response;

        try {
            request.setAction(action);
            response = bean.processDataType(request);
            LOGGER.info(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime));
        } catch (RuntimeException e) {
            LOGGER.error(LoggingUtil.requestDuration(settings.getLocale(), restAction, startTime, e), e);
            response = new ProcessDataTypeResponse(ReturnCode.ERROR, e.getMessage());
        }

        return RestUtils.buildResponse(response);
    }
}
