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
package net.haugr.cws.core.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import net.haugr.cws.core.model.entities.CircleEntity;
import net.haugr.cws.core.model.entities.MemberEntity;
import net.haugr.cws.core.model.entities.TrusteeEntity;

/**
 * <p>Data Access Object functionality used explicitly for the fetching &amp;
 * processing of Members.</p>
 *
 * @author Kim Jensen
 * @since CWS 1.1
 */
public final class MemberDao extends CommonDao {

    public MemberDao(final EntityManager entityManager) {
        super(entityManager);
    }

    public List<CircleEntity> findCirclesForMember(final MemberEntity member) {
        final Query query = entityManager
                .createNamedQuery("trustee.findCirclesByMember")
                .setParameter(MEMBER, member);

        return findList(query);
    }

    public List<TrusteeEntity> findCirclesBothBelongTo(final MemberEntity member, final MemberEntity requested) {
        final Query query = entityManager
                .createNamedQuery("trustee.findSharedCircles")
                .setParameter(MEMBER, member)
                .setParameter("requested", requested);

        return findList(query);
    }
}
