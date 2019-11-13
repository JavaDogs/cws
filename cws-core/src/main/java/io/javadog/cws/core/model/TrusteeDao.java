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

import io.javadog.cws.core.model.entities.TrusteeEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * <p>Data Access Object functionality used explicitly for the fetching &amp;
 * processing of Trustees.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.1
 */
public final class TrusteeDao extends CommonDao {

    public TrusteeDao(final EntityManager entityManager) {
        super(entityManager);
    }

    public List<TrusteeEntity> findTrusteesByMemberAndCircle(final String externalMemberId, final String externalCircleId) {
        final Query query = entityManager.createNamedQuery("trustee.findByCircleAndMember");
        query.setParameter("emid", externalMemberId);
        query.setParameter("ecid", externalCircleId);

        return findList(query);
    }

    public List<TrusteeEntity> findTrusteesByMember(final String externalMemberId) {
        final Query query = entityManager.createNamedQuery("trustee.findByExternalMemberId");
        query.setParameter("externalMemberId", externalMemberId);

        return findList(query);
    }

    public List<TrusteeEntity> findTrusteesByCircle(final String externalCircleId) {
        final Query query = entityManager.createNamedQuery("trustee.findByExternalCircleId");
        query.setParameter("externalCircleId", externalCircleId);

        return findList(query);
    }
}