package io.javadog.cws.core.services;

import io.javadog.cws.api.common.Constants;
import io.javadog.cws.api.requests.SettingRequest;
import io.javadog.cws.api.responses.SettingResponse;
import io.javadog.cws.common.Settings;
import io.javadog.cws.common.exceptions.CWSException;
import io.javadog.cws.core.Action;
import io.javadog.cws.core.Servicable;

import javax.persistence.EntityManager;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class SettingService extends Servicable<SettingResponse, SettingRequest> {

    public SettingService(final Settings settings, final EntityManager entityManager) {
        super(settings, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SettingResponse process(final SettingRequest request) {
        verifyAndCheckRequest(request, Action.SETTING);

        throw new CWSException(Constants.NOTIMPLEMENTED_ERROR, "Not Yet Implemented.");
    }
}
