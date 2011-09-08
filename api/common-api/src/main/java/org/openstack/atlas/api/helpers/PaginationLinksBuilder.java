package org.openstack.atlas.api.helpers;

import org.openstack.atlas.docs.loadbalancers.api.v1.Link;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class PaginationLinksBuilder {
    private final static String NEXT_LINK = "next";
    private final static String PREVIOUS_LINK = "previous";
//    private final static String SELF = "self";
//    private final static Integer HARD_LIMIT = 100;
    private final static Integer HARD_MARKER = 0;

    public static List<Link> buildLinks(UriInfo uriInfo, List<Integer> allObjectIds, List<Integer> paginatedObjectIds, Integer limit, Integer marker) throws UnexpectedException {
        boolean PREVIOUS = true;
        boolean NEXT = true;

        List<Link> links = new ArrayList<Link>();
        if (!paginatedObjectIds.isEmpty()) {
            if (marker == null) marker = HARD_MARKER;
                if (marker >= allObjectIds.get(allObjectIds.size() - 1) || allObjectIds.size() == paginatedObjectIds.size()) NEXT = false;
            if (marker <= allObjectIds.get(0)) PREVIOUS = false;

            if (NEXT) {
                int lastId = allObjectIds.indexOf(paginatedObjectIds.get(paginatedObjectIds.size() - 1));
                UriBuilder uriBuilderNext = null;
                try {
                    uriBuilderNext = buildUri(uriInfo, (limit), allObjectIds.get(lastId + 1));
                } catch (IndexOutOfBoundsException ex) {
//                    uriBuilderNext = buildUri(uriInfo, (limit), allObjectIds.get(lastId));
                    //empty
                }

                Link nextLink = new Link();
                nextLink.setHref(uriBuilderNext.build().toString());
                nextLink.setRel(NEXT_LINK);
                links.add(nextLink);
            }

            if (PREVIOUS) {
                int firstId = allObjectIds.indexOf(paginatedObjectIds.get(0));
                UriBuilder uriBuilderPrevious;
                try {
                    uriBuilderPrevious = buildUri(uriInfo, (limit), allObjectIds.get(firstId - limit));
                } catch (IndexOutOfBoundsException ex) {
                    uriBuilderPrevious = buildUri(uriInfo, (limit), allObjectIds.get(firstId));
                }

                Link previousLink = new Link();
                previousLink.setHref(uriBuilderPrevious.build().toString());
                previousLink.setRel(PREVIOUS_LINK);
                links.add(previousLink);
            }
        }
        return links;
    }

    private static UriBuilder buildUri(UriInfo uriInfo, Integer limit, Integer marker) {
        return uriInfo.getAbsolutePathBuilder().replaceQuery("marker=" + marker + "&limit=" + limit);
    }
}
