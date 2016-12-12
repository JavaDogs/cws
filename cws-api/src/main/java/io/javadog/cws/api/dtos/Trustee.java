package io.javadog.cws.api.dtos;

import io.javadog.cws.api.common.TrustWorthiness;

import java.util.Date;

/**
 * <p>A Trustee, is a member of a Circle, with a level of Trustworthiness, and
 * information about </p>
 *
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class Trustee {

    private Circle circle = null;
    private Member member = null;
    private TrustWorthiness trustWorthiness = TrustWorthiness.NONE;
    private Date modified = null;
    private Date added = null;
}
