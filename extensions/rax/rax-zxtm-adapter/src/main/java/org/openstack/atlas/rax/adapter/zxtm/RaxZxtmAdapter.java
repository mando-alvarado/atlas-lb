package org.openstack.atlas.rax.adapter.zxtm;

import org.openstack.atlas.adapter.LoadBalancerEndpointConfiguration;
import org.openstack.atlas.adapter.exception.AdapterException;
import org.openstack.atlas.service.domain.entity.LoadBalancer;
import org.openstack.atlas.service.domain.entity.VirtualIp;
import org.openstack.atlas.service.domain.entity.VirtualIpv6;

import java.util.List;
import java.util.Set;

public interface RaxZxtmAdapter {
    void addVirtualIps(LoadBalancerEndpointConfiguration config, Integer accountId, Integer lbId, Set<VirtualIp> ipv4Vips, Set<VirtualIpv6> ipv6Vips) throws AdapterException;

    void deleteVirtualIps(LoadBalancerEndpointConfiguration config, LoadBalancer lb, List<Integer> vipIdsToDelete) throws AdapterException;
}
