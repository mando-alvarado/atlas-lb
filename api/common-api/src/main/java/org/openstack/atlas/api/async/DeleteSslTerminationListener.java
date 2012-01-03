package org.openstack.atlas.api.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openstack.atlas.service.domain.entities.LoadBalancer;
import org.openstack.atlas.service.domain.entities.LoadBalancerStatus;
import org.openstack.atlas.service.domain.events.UsageEvent;
import org.openstack.atlas.service.domain.exceptions.EntityNotFoundException;
import org.openstack.atlas.service.domain.pojos.MessageDataContainer;

import javax.jms.Message;

import static org.openstack.atlas.service.domain.events.entities.CategoryType.DELETE;
import static org.openstack.atlas.service.domain.events.entities.EventSeverity.CRITICAL;
import static org.openstack.atlas.service.domain.events.entities.EventSeverity.INFO;
import static org.openstack.atlas.service.domain.events.entities.EventType.DELETE_LOADBALANCER;
import static org.openstack.atlas.service.domain.events.entities.EventType.DELETE_SSL_TERMINATION;
import static org.openstack.atlas.service.domain.services.helpers.AlertType.DATABASE_FAILURE;
import static org.openstack.atlas.service.domain.services.helpers.AlertType.ZEUS_FAILURE;

public class DeleteSslTerminationListener extends BaseListener {

    private final Log LOG = LogFactory.getLog(DeleteSslTerminationListener.class);

    @Override
    public void doOnMessage(final Message message) throws Exception {
        LOG.debug("Entering " + getClass());
        LOG.debug(message);

        MessageDataContainer dataContainer = getDataContainerFromMessage(message);
        LoadBalancer dbLoadBalancer;
        LoadBalancer queueLb = new LoadBalancer();
        queueLb.setUserName(dataContainer.getUserName());
        queueLb.setId(dataContainer.getLoadBalancerId());
        queueLb.setAccountId(dataContainer.getAccountId());

        try {
            dbLoadBalancer = loadBalancerService.get(queueLb.getId());
        } catch (EntityNotFoundException enfe) {
            String alertDescription = String.format("Load balancer '%d' not found in database.", queueLb.getId());
            LOG.error(alertDescription, enfe);
            notificationService.saveAlert(queueLb.getAccountId(), queueLb.getId(), enfe, DATABASE_FAILURE.name(), alertDescription);
            sendErrorToEventResource(queueLb);
            return;
        }

        try {
            LOG.debug(String.format("Deleting load balancer '%d' ssl termination in Zeus...", dbLoadBalancer.getId()));
            reverseProxyLoadBalancerService.removeSslTermination(queueLb.getId(), queueLb.getAccountId());
            LOG.debug(String.format("Successfully deleted load balancer ssl termination '%d' in Zeus.", dbLoadBalancer.getId()));
        } catch (Exception e) {
            loadBalancerService.setStatus(dbLoadBalancer, LoadBalancerStatus.ERROR);
            LOG.error(String.format("LoadBalancer status before error was: '%s'", dbLoadBalancer.getStatus()));
            String alertDescription = String.format("Error deleting loadbalancer '%d' ssl termination in Zeus.", dbLoadBalancer.getId());
            LOG.error(alertDescription, e);
            notificationService.saveAlert(dbLoadBalancer.getAccountId(), dbLoadBalancer.getId(), e, ZEUS_FAILURE.name(), alertDescription);
            sendErrorToEventResource(queueLb);
            // Notify usage processor with a usage event
            notifyUsageProcessor(message, dbLoadBalancer, UsageEvent.SSL_OFF);
            return;
        }

        sslTerminationService.deleteSslTermination(dbLoadBalancer.getId(), dbLoadBalancer.getAccountId());

        // Add atom entry
        String atomTitle = "Load Balancer SSL Termination Successfully Deleted";
        String atomSummary = "Load balancer ssl termination successfully deleted";
        notificationService.saveLoadBalancerEvent(queueLb.getUserName(), dbLoadBalancer.getAccountId(), dbLoadBalancer.getId(), atomTitle, atomSummary, DELETE_SSL_TERMINATION, DELETE, INFO);

        // Notify usage processor with a usage event
        notifyUsageProcessor(message, dbLoadBalancer, UsageEvent.SSL_OFF);

        LOG.info(String.format("Load balancer ssl termination '%d' successfully deleted.", dbLoadBalancer.getId()));
    }

    private void sendErrorToEventResource(LoadBalancer lb) {
        String title = "Error Deleting Load Balancer ssl termination";
        String desc = "Could not delete the load balancer ssl termination at this time.";
        notificationService.saveLoadBalancerEvent(lb.getUserName(), lb.getAccountId(), lb.getId(), title, desc, DELETE_SSL_TERMINATION, DELETE, CRITICAL);
    }

}
