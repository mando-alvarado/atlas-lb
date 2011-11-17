package org.openstack.atlas.rax.domain.service.impl;

import org.openstack.atlas.core.api.v1.LoadBalancer;
import org.openstack.atlas.rax.domain.entity.AccessList;
import org.openstack.atlas.rax.domain.repository.RaxAccessListRepository;
import org.openstack.atlas.rax.domain.service.RaxAccessListService;
import org.openstack.atlas.service.domain.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RaxAccessListServiceImpl implements RaxAccessListService {

    @Autowired
    private RaxAccessListRepository accessListRepository;

//    @Override
    public List<AccessList> getAccessListByAccountIdLoadBalancerId(int accountId, int loadbalancerId, Integer offset, Integer limit, Integer marker) throws EntityNotFoundException, DeletedStatusException {
        return accessListRepository.getAccessListByAccountIdLoadBalancerId(accountId, loadbalancerId, offset, limit, marker);
    }

//    @Override
    public LoadBalancer markForDeletionNetworkItems(LoadBalancer returnLB, List<Integer> networkItemIds) throws BadRequestException, ImmutableEntityException {
        return null;
    }

//    @Override
    public LoadBalancer updateAccessList(LoadBalancer rLb) throws ImmutableEntityException, BadRequestException, UnprocessableEntityException {
        return null;
    }

//    @Override
    public Set<AccessList> diffRequestAccessListWithDomainAccessList(LoadBalancer rLb, org.openstack.atlas.service.domain.entity.LoadBalancer dLb) {
        return null;
    }

//    @Override
    public LoadBalancer markForDeletionAccessList(LoadBalancer rLb) throws ImmutableEntityException, DeletedStatusException, UnprocessableEntityException {
        return null;
    }

//    @Override
    public LoadBalancer markForDeletionNetworkItem(LoadBalancer rLb) throws ImmutableEntityException {
        return null;
    }
}