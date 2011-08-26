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

public class LinkageUriBuilder {
    private final static String NEXT = "next";
    private final static String PREVIOUS = "previous";
    private final static String SELF = "self";

    public static List<Link> buildLinks(UriInfo uriInfo, Collection<?> objects, Integer limit, Integer marker) throws UnexpectedException {

        List<Link> links = new ArrayList<Link>();

        if (objects.size() > 0) {
            for (Object ob : objects) {
               LoadBalancer lb = (LoadBalancer) ob;
                lb.getId();
            }
            UriBuilder uriBuilderNext = buildUri(uriInfo, (limit), (limit + marker));
            UriBuilder uriBuilderPrevious = buildUri(uriInfo, (limit), (limit - limit));
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
