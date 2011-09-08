package org.openstack.atlas.api.helpers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.openstack.atlas.docs.loadbalancers.api.v1.Link;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class PaginationLinksHelperTest {
    public static class WhenRetrievingLinksFromPaginatedResults {
        List<Integer> paginatedLbIdList;
        List<Integer> paginatedLbIdList2;
        List<Integer> allLbIdList;
        UriBuilder uriBuilder = UriBuilder.fromUri("http://127.0.1.1/pub/test/loadbalancers?marker=0&limit=2");
        UriInfo uriInfo = mock(UriInfo.class);

        @Before
        public void Set_up_lists() {
            paginatedLbIdList = new ArrayList<Integer>();
            paginatedLbIdList.add(1);
            paginatedLbIdList.add(4);
            paginatedLbIdList.add(7);
            paginatedLbIdList.add(9);
            paginatedLbIdList.add(35);

            paginatedLbIdList2 = new ArrayList<Integer>();
            paginatedLbIdList2.add(40);
            paginatedLbIdList2.add(52);
            paginatedLbIdList2.add(69);
            paginatedLbIdList2.add(71);
            paginatedLbIdList2.add(90);

            allLbIdList = new ArrayList<Integer>();
            allLbIdList.add(1);
            allLbIdList.add(4);
            allLbIdList.add(7);
            allLbIdList.add(9);
            allLbIdList.add(35);
            allLbIdList.add(40);
            allLbIdList.add(52);
            allLbIdList.add(69);
            allLbIdList.add(71);
            allLbIdList.add(90);
        }

        @Test
        public void Should_return_proper_next_link() throws UnexpectedException, URISyntaxException {
            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            List<Link> bla = PaginationLinksBuilder.buildLinks(uriInfo, allLbIdList, paginatedLbIdList, 1, 0);
            for (Link link : bla) {
                Assert.assertEquals(link.getRel(), "next");
                Assert.assertEquals("http://127.0.1.1/pub/test/loadbalancers?marker=40&limit=1", link.getHref());
                break;
            }
        }

        @Test
        public void Should_return_without_next_link() throws UnexpectedException {
            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            for (Link link : PaginationLinksBuilder.buildLinks(uriInfo, allLbIdList, paginatedLbIdList2, 1, 90)) {
                if (link.getRel().equals("next")) throw new UnexpectedException("the next link is still here");
            }
        }

        @Test
        public void Should_return_without_previous_link() throws UnexpectedException, URISyntaxException {
            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            for (Link link : PaginationLinksBuilder.buildLinks(uriInfo, allLbIdList, paginatedLbIdList, 1, 0)) {
                if (link.getRel().equals("previous")) throw new UnexpectedException("the previous link is still here");
            }
        }

        @Test
        public void Should_return_proper_previous_link() throws UnexpectedException, URISyntaxException {
            when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
            for (Link link : PaginationLinksBuilder.buildLinks(uriInfo, allLbIdList, paginatedLbIdList, 1, 9)) {
                if (link.getRel().equals("previous")) {
                Assert.assertEquals(link.getRel(), "previous");
                Assert.assertEquals("http://127.0.1.1/pub/test/loadbalancers?marker=1&limit=1", link.getHref());
                break;
                }
            }
        }
    }
}
