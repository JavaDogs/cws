/*
 * =============================================================================
 * Copyright (c) 2016-2017, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-soap)
 * =============================================================================
 */
package io.javadog.cws.soap;

import io.javadog.cws.api.requests.FetchCircleRequest;
import io.javadog.cws.api.requests.FetchMemberRequest;
import io.javadog.cws.api.requests.ProcessCircleRequest;
import io.javadog.cws.api.requests.ProcessMemberRequest;
import io.javadog.cws.api.requests.SanityRequest;
import io.javadog.cws.api.requests.SettingRequest;
import io.javadog.cws.api.responses.FetchCircleResponse;
import io.javadog.cws.api.responses.FetchMemberResponse;
import io.javadog.cws.api.responses.ProcessCircleResponse;
import io.javadog.cws.api.responses.ProcessMemberResponse;
import io.javadog.cws.api.responses.SanityResponse;
import io.javadog.cws.api.responses.SettingResponse;
import io.javadog.cws.api.responses.VersionResponse;
import io.javadog.cws.common.Settings;
import io.javadog.cws.common.exceptions.CWSException;
import io.javadog.cws.core.Serviceable;
import io.javadog.cws.core.services.FetchCircleService;
import io.javadog.cws.core.services.FetchMemberService;
import io.javadog.cws.core.services.ProcessCircleService;
import io.javadog.cws.core.services.ProcessMemberService;
import io.javadog.cws.core.services.SanityService;
import io.javadog.cws.core.services.SettingService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Logger;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
@Stateless
public class SystemBean {

    private static final Logger log = Logger.getLogger(SystemBean.class.getName());

    @PersistenceContext(unitName = "cwsDS")
    private EntityManager entityManager;

    @Inject private SettingBean settingBean;

    @Transactional(Transactional.TxType.SUPPORTS)
    public VersionResponse version() {
        final VersionResponse response = new VersionResponse();
        response.setVersion("0.7-SNAPSHOT");

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public SettingResponse settings(final SettingRequest request) {
        Serviceable<SettingResponse, SettingRequest> service = null;
        SettingResponse response;

        try {
            service = new SettingService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new SettingResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SanityResponse sanity(final SanityRequest request) {
        Serviceable<SanityResponse, SanityRequest> service = null;
        SanityResponse response;

        try {
            service = new SanityService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new SanityResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public FetchMemberResponse fetchMembers(final FetchMemberRequest request) {
        Serviceable<FetchMemberResponse, FetchMemberRequest> service = null;
        FetchMemberResponse response;

        try {
            service = new FetchMemberService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new FetchMemberResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public ProcessMemberResponse processMember(final ProcessMemberRequest request) {
        Serviceable<ProcessMemberResponse, ProcessMemberRequest> service = null;
        ProcessMemberResponse response;

        try {
            service = new ProcessMemberService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new ProcessMemberResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public FetchCircleResponse fetchCircles(final FetchCircleRequest request) {
        Serviceable<FetchCircleResponse, FetchCircleRequest> service = null;
        FetchCircleResponse response;

        try {
            service = new FetchCircleService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new FetchCircleResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public ProcessCircleResponse processCircle(final ProcessCircleRequest request) {
        Serviceable<ProcessCircleResponse, ProcessCircleRequest> service = null;
        ProcessCircleResponse response;

        try {
            service = new ProcessCircleService(settingBean.getSettings(), entityManager);
            response = service.perform(request);
        } catch (CWSException e) {
            // Any Warning or Error thrown by the CWS contain enough information
            // so it can be dealt with by the requesting System. Logging the
            // error is thus not needed, as all information is provided in the
            // response.
            log.log(Settings.DEBUG, e.getMessage(), e);
            response = new ProcessCircleResponse(e.getReturnCode(), e.getMessage());
        } finally {
            CommonBean.destroy(service);
        }

        return response;
    }
}