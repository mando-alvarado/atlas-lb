package org.openstack.atlas.rax.domain.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openstack.atlas.rax.domain.entity.AccessList;
import org.openstack.atlas.service.domain.exception.PersistenceServiceException;
import org.openstack.atlas.service.domain.service.ExtraFeatureService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RaxAccessListServiceImpl implements ExtraFeatureService<AccessList> {
    private final Log LOG = LogFactory.getLog(AccessListServiceImpl.class);

    @Override
    public AccessList update(Integer loadBalancerId, AccessList objectToUpdate) throws PersistenceServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void preDelete(Integer loadBalancerId) throws PersistenceServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Integer loadBalancerId) throws PersistenceServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

/*

    @Autowired
    protected LoadBalancerRepository loadBalancerRepository;
    @Autowired
    protected ConnectionThrottleRepository connectionThrottleRepository;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ImmutableEntityException.class, UnprocessableEntityException.class})
    public ConnectionThrottle update(Integer loadBalancerId, ConnectionThrottle connectionThrottle) throws PersistenceServiceException {
        LoadBalancer dbLoadBalancer = loadBalancerRepository.getById(loadBalancerId);
        ConnectionThrottle dbConnectionThrottle = dbLoadBalancer.getConnectionThrottle();

        if(dbConnectionThrottle == null) {
            dbConnectionThrottle = connectionThrottle;
            dbConnectionThrottle.setLoadBalancer(dbLoadBalancer);
        }

        setPropertiesForUpdate(connectionThrottle, dbConnectionThrottle);

        loadBalancerRepository.changeStatus(dbLoadBalancer.getAccountId(), dbLoadBalancer.getId(), CoreLoadBalancerStatus.PENDING_UPDATE, false);
        dbLoadBalancer.setConnectionThrottle(dbConnectionThrottle);
        dbLoadBalancer = loadBalancerRepository.update(dbLoadBalancer);
        return dbLoadBalancer.getConnectionThrottle();
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class})
    public void preDelete(Integer loadBalancerId) throws EntityNotFoundException {
        LoadBalancer dbLoadBalancer = loadBalancerRepository.getById(loadBalancerId);
        if (dbLoadBalancer.getConnectionThrottle() == null)
            throw new EntityNotFoundException("Connection throttle not found");
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class})
    public void delete(Integer loadBalancerId) throws EntityNotFoundException {
        connectionThrottleRepository.delete(connectionThrottleRepository.getByLoadBalancerId(loadBalancerId));
    }

    protected void setPropertiesForUpdate(final ConnectionThrottle requestConnectionThrottle, final ConnectionThrottle dbConnectionThrottle) throws BadRequestException {
        if (requestConnectionThrottle.getMaxRequestRate() != null)
            dbConnectionThrottle.setMaxRequestRate(requestConnectionThrottle.getMaxRequestRate());
        else throw new BadRequestException("Must provide a max request rate for the request");

        if (requestConnectionThrottle.getRateInterval() != null)
            dbConnectionThrottle.setRateInterval(requestConnectionThrottle.getRateInterval());
        else throw new BadRequestException("Must provide a rate interval for the request");
    }
}
