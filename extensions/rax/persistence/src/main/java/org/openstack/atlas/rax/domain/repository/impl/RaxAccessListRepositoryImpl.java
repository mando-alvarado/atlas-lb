package org.openstack.atlas.rax.domain.repository.impl;

import org.openstack.atlas.rax.domain.entity.AccessList;
import org.openstack.atlas.rax.domain.repository.RaxAccessListRepository;
import org.openstack.atlas.service.domain.entity.LoadBalancer;
import org.openstack.atlas.service.domain.exception.DeletedStatusException;
import org.openstack.atlas.service.domain.exception.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class RaxAccessListRepositoryImpl implements RaxAccessListRepository {

    private EntityManager entityManager;
    private RaxLoadBalancerRepositoryImpl loadBalancerRepository;

    public AccessList getNetworkItemByAccountIdLoadBalancerIdNetworkItemId(Integer aid, Integer lid, Integer nid) throws EntityNotFoundException {
        List<AccessList> al = null;
        String qStr = "SELECT a FROM AccessList a WHERE a.loadbalancer.id = :lid AND a.loadbalancer.accountId = :aid AND a.id = :nid";

        if (lid == null || aid == null || nid == null) {
            throw new EntityNotFoundException("Null parameter Query rejected");
        }

        Query q = entityManager.createQuery(qStr);
        q.setParameter("aid", aid);
        q.setParameter("lid", lid);
        q.setParameter("nid", nid);
        q.setMaxResults(1);
        al = q.getResultList();
        if (al.size() != 1) {
            throw new EntityNotFoundException("Node not nound");
        }
        return al.get(0);
    }

    public List<AccessList> getAccessListByAccountIdLoadBalancerId(int accountId,
                                int loadbalancerId,Integer... p) throws EntityNotFoundException, DeletedStatusException {
        LoadBalancer lb = loadBalancerRepository.getByIdAndAccountId(loadbalancerId, accountId);
        List<AccessList> accessList = new ArrayList<AccessList>();
        if (lb.getStatus().equals("DELETED")) {
            throw new DeletedStatusException("The loadbalancer is marked as deleted.");
        }
        Query query = entityManager.createQuery("FROM AccessList a WHERE a.loadbalancer.id = :lid AND a.loadbalancer.accountId = :aid")
                .setParameter("lid", loadbalancerId).setParameter("aid", accountId);

        if (p.length >= 3) {
            Integer offset = p[0];
            Integer limit = p[1];
            Integer marker = p[2];
            if (offset == null) {
                offset = 0;
            }
            if (limit == null || limit > 100) {
                limit = 100;
            }
            if (marker != null) {
                query = entityManager.createQuery("FROM AccessList a WHERE a.loadbalancer.id = :lid AND a.loadbalancer.accountId = :aid AND a.id >= :accessId")
                        .setParameter("lid", loadbalancerId).setParameter("aid", accountId).setParameter("accessId", marker);
            }
            query = query.setFirstResult(offset).setMaxResults(limit);
        }
        accessList = query.getResultList();
        return accessList;

    }
}