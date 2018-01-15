/*
 * =============================================================================
 * Copyright (c) 2016-2018, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-soap)
 * =============================================================================
 */
package io.javadog.cws.soap;

import io.javadog.cws.api.Management;
import io.javadog.cws.api.common.ReturnCode;
import io.javadog.cws.api.requests.FetchCircleRequest;
import io.javadog.cws.api.requests.FetchMemberRequest;
import io.javadog.cws.api.requests.FetchTrusteeRequest;
import io.javadog.cws.api.requests.ProcessCircleRequest;
import io.javadog.cws.api.requests.ProcessMemberRequest;
import io.javadog.cws.api.requests.ProcessTrusteeRequest;
import io.javadog.cws.api.requests.SanityRequest;
import io.javadog.cws.api.requests.SettingRequest;
import io.javadog.cws.api.responses.FetchCircleResponse;
import io.javadog.cws.api.responses.FetchMemberResponse;
import io.javadog.cws.api.responses.FetchTrusteeResponse;
import io.javadog.cws.api.responses.ProcessCircleResponse;
import io.javadog.cws.api.responses.ProcessMemberResponse;
import io.javadog.cws.api.responses.ProcessTrusteeResponse;
import io.javadog.cws.api.responses.SanityResponse;
import io.javadog.cws.api.responses.SettingResponse;
import io.javadog.cws.api.responses.VersionResponse;
import io.javadog.cws.core.ManagementBean;
import io.javadog.cws.core.misc.LoggingUtil;
import io.javadog.cws.core.model.Settings;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;
import java.util.logging.Logger;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
@SOAPBinding
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
@WebService(name = "management", targetNamespace = "http://ws.cws.javadog.io/", serviceName = "management", portName = "management")
public class ManagementService implements Management {

    private static final Logger log = Logger.getLogger(ManagementService.class.getName());

    private static final String GENERAL_RETURN_MESSAGE = "An unknown error occurred. Please consult the CWS System Log.";

    private final Settings settings = Settings.getInstance();
    @Inject private ManagementBean bean;

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public VersionResponse version() {
        VersionResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.version();
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "version", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new VersionResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public SettingResponse settings(@WebParam(name = "request") final SettingRequest request) {
        SettingResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.settings(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "settings", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new SettingResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public SanityResponse sanitized(@WebParam(name = "request") final SanityRequest request) {
        SanityResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.sanity(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "sanitized", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new SanityResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public FetchMemberResponse fetchMembers(@WebParam(name = "request") final FetchMemberRequest request) {
        FetchMemberResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.fetchMembers(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "fetchMembers", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new FetchMemberResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public ProcessMemberResponse processMember(@WebParam(name = "request") final ProcessMemberRequest request) {
        ProcessMemberResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.processMember(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "processMember", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new ProcessMemberResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public FetchCircleResponse fetchCircles(@WebParam(name = "request") final FetchCircleRequest request) {
        FetchCircleResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.fetchCircles(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "fetchCircles", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new FetchCircleResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public ProcessCircleResponse processCircle(@WebParam(name = "request") final ProcessCircleRequest request) {
        ProcessCircleResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.processCircle(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "processCircle", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new ProcessCircleResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public FetchTrusteeResponse fetchTrustees(@WebParam(name = "request") final FetchTrusteeRequest request) {
        FetchTrusteeResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.fetchTrustees(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "fetchCircles", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new FetchTrusteeResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @WebMethod
    @WebResult(name = "response")
    public ProcessTrusteeResponse processTrustee(@WebParam(name = "request") final ProcessTrusteeRequest request) {
        ProcessTrusteeResponse response;

        try {
            final Long startTime = java.lang.System.nanoTime();
            response = bean.processTrustee(request);
            log.log(Settings.INFO, () -> LoggingUtil.requestDuration(settings.getLocale(), "processCircle", startTime));
        } catch (RuntimeException e) {
            // If an error occurs that has so far not been resolved, this is the
            // final level where it can be handled. Errors can be Persistence
            // problems or other things that will affect the reliability and/or
            // performance of the system.
            log.log(Settings.ERROR, e.getMessage(), e);
            response = new ProcessTrusteeResponse(ReturnCode.ERROR, GENERAL_RETURN_MESSAGE);
        }

        return response;
    }
}
