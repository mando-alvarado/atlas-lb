package org.openstack.atlas.rax.domain.entity;

import org.openstack.atlas.service.domain.entity.UsageRecord;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import java.io.Serializable;

@javax.persistence.Entity
@DiscriminatorValue("RAX")
public class RaxUsageRecord extends UsageRecord implements Serializable {
    private final static long serialVersionUID = 532512316L;

    @Column(name = "avg_concurrent_conns", nullable = false)
    Double averageConcurrentConnections = 0.0;
    @Column(name = "num_polls", nullable = false)
    Integer numberOfPolls = 0;

    public Double getAverageConcurrentConnections() {
        return averageConcurrentConnections;
    }

    public void setAverageConcurrentConnections(Double averageConcurrentConnections) {
        this.averageConcurrentConnections = averageConcurrentConnections;
    }

    public Integer getNumberOfPolls() {
        return numberOfPolls;
    }

    public void setNumberOfPolls(Integer numberOfPolls) {
        this.numberOfPolls = numberOfPolls;
    }

    @Override
    public String toString() {
        return "RaxUsageRecord{" +
                "averageConcurrentConnections=" + averageConcurrentConnections +
                ", numberOfPolls=" + numberOfPolls +
                '}';
    }
}
