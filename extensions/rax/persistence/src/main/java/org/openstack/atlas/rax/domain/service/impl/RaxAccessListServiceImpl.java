package org.openstack.atlas.rax.domain.service.impl;

import org.openstack.atlas.core.api.v1.LoadBalancer;
import org.openstack.atlas.rax.domain.entity.AccessList;
import org.openstack.atlas.rax.domain.service.AccessListService;
import org.openstack.atlas.service.domain.exception.BadRequestException;
import org.openstack.atlas.service.domain.exception.DeletedStatusException;
import org.openstack.atlas.service.domain.exception.ImmutableEntityException;
import org.openstack.atlas.service.domain.exception.UnprocessableEntityException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
public class RaxAccessListServiceImpl implements AccessListService {

    @Override
    public List<AccessList> getAccessListByAccountIdLoadBalancerId(int accountId, int loadbalancerId, Integer... p) throws EntityNotFoundException, DeletedStatusException {
        return null;
    }

    @Override
    public LoadBalancer markForDeletionNetworkItems(LoadBalancer returnLB, List<Integer> networkItemIds) throws BadRequestException, EntityNotFoundException, ImmutableEntityException {
        return null;
    }

    @Override
    public LoadBalancer updateAccessList(LoadBalancer rLb) throws EntityNotFoundException, ImmutableEntityException, BadRequestException, UnprocessableEntityException {
        return null;
    }

    @Override
    public Set<AccessList> diffRequestAccessListWithDomainAccessList(LoadBalancer rLb, org.openstack.atlas.service.domain.entity.LoadBalancer dLb) {
        return null;
    }

    @Override
    public LoadBalancer markForDeletionAccessList(LoadBalancer rLb) throws EntityNotFoundException, ImmutableEntityException, DeletedStatusException, UnprocessableEntityException {
        return null;
    }

    @Override
    public LoadBalancer markForDeletionNetworkItem(LoadBalancer rLb) throws EntityNotFoundException, ImmutableEntityException {
        return null;
    }
}