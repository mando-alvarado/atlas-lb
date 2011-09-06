package org.openstack.atlas.api.helpers;

import org.openstack.atlas.docs.loadbalancers.api.v1.Link;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class LinkageUriBuilder {
    private final static String NEXT = "next";
    private final static String PREVIOUS = "previous";
    private final static String SELF = "self";
    private final static Integer HARD_LIMIT = 100;
    private final static Integer HARD_MARKER = 0;

    public static List<Link> buildLinks(UriInfo uriInfo, List<Integer> idList, List<Integer> pagilbIds, Integer limit, Integer marker) throws UnexpectedException {

        if (limit == null || limit < 0 || limit > 100) {
            limit = HARD_LIMIT;
        }

        if (marker == null) {
            marker = HARD_MARKER;
        }

        List<Link> links = new ArrayList<Link>();
        if (idList.size() > 0) {

            Boolean prev = true;
            Boolean next = true;

            if (marker.equals(idList.get(0))) {
                prev = false;
            }
            
            if (marker.equals(idList.get(idList.size() - 1))) {
                next = false;
            }

            if (next) {
                int pointer;
                if (marker.equals(0)) {
                   marker = idList.get(idList.indexOf(pagilbIds.get(pagilbIds.size() - 1)));
                }

                UriBuilder uriBuilderNext = buildUri(uriInfo, (limit), (marker + limit));
                Link nextLink = new Link();
                nextLink.setHref(uriBuilderNext.build().toString());
                nextLink.setRel(NEXT);
                links.add(nextLink);
            }

            if (prev) {
                UriBuilder uriBuilderPrevious = buildUri(uriInfo, (limit), (idList.get(idList.indexOf(marker) - limit)));
                Link previousLink = new Link();
                previousLink.setHref(uriBuilderPrevious.build().toString());
                previousLink.setRel(PREVIOUS);
                links.add(previousLink);
            }

        } else {
            throw new UnexpectedException("The list is empty");
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
