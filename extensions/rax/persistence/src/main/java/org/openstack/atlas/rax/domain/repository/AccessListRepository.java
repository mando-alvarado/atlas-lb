package org.openstack.atlas.rax.domain.repository;

import org.openstack.atlas.rax.domain.entity.AccessList;
import org.openstack.atlas.service.domain.exception.DeletedStatusException;
import org.openstack.atlas.service.domain.exception.EntityNotFoundException;

import java.util.List;

public interface AccessListRepository {

    public AccessList getNetworkItemByAccountIdLoadBalancerIdNetworkItemId(Integer aid, Integer lid, Integer nid) throws EntityNotFoundException;
    public List<AccessList> getAccessListByAccountIdLoadBalancerId(int accountId, int loadbalancerId,Integer... p) throws EntityNotFoundException, DeletedStatusException;

}