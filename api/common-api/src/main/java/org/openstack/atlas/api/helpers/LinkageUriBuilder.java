package org.openstack.atlas.api.helpers;

import org.openstack.atlas.docs.loadbalancers.api.v1.Link;
import org.openstack.atlas.docs.loadbalancers.api.v1.LoadBalancer;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class LinkageUriBuilder {
    private final static String NEXT = "next";
    private final static String PREVIOUS = "previous";
    private final static String SELF = "self";
    private final static Integer HARD_LIMIT = 100;
    private final static Integer HARD_MARKER = 0;

    public static List<Link> buildLinks(UriInfo uriInfo, List<Integer> idList, Integer limit, Integer marker) throws UnexpectedException {

        if (limit == null || limit < 0 || limit > 100) {
            limit = HARD_LIMIT;
        }

        if (marker == null) {
            marker = HARD_MARKER;
        }

        List<Link> links = new ArrayList<Link>();
        if (idList.size() > 0) {
            UriBuilder uriBuilderNext = buildUri(uriInfo, (limit), (idList.get(idList.size() - 1)));
            UriBuilder uriBuilderPrevious = buildUri(uriInfo, (limit), (idList.get(0)));
            UriBuilder uriBuilderself = buildUri(uriInfo, (limit), (marker));

            Link nextLink = new Link();
            nextLink.setHref(uriBuilderNext.build().toString());
            nextLink.setRel(NEXT);
            links.add(nextLink);

            Link previousLink = new Link();
            previousLink.setHref(uriBuilderPrevious.build().toString());
            previousLink.setRel(PREVIOUS);
            links.add(previousLink);

            Link selfLink = new Link();
            selfLink.setHref(uriBuilderself.build().toString());
            selfLink.setRel(SELF);
            links.add(selfLink);
        } else {
            throw new UnexpectedException("Something happened!");
        }
        return links;
    }

    private static String splitUri(UriInfo uriInfo) {
        return uriInfo.getBaseUri().toString();
    }

    private static UriBuilder buildUri(UriInfo uriInfo, Integer limit, Integer marker) {
        return uriInfo.getAbsolutePathBuilder().replaceQuery("marker=" + marker + "&limit=" + limit);
    }
}
