package org.openstack.atlas.service.domain.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openstack.atlas.service.domain.entities.BlacklistItem;
import org.openstack.atlas.service.domain.entities.BlacklistType;
import org.openstack.atlas.service.domain.entities.Node;
import org.openstack.atlas.service.domain.exceptions.EntityNotFoundException;
import org.openstack.atlas.service.domain.services.BlackListService;
import org.openstack.atlas.util.ip.exception.IPStringConversionException;
import org.openstack.atlas.util.ip.exception.IpTypeMissMatchException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlackListServiceImpl extends BaseService implements BlackListService {

    final Log LOG = LogFactory.getLog(BlackListServiceImpl.class);

    @Override
    @Transactional
    public void deleteBlackList(BlacklistItem msgBlacklist) throws Exception {
        LOG.debug("Entering " + getClass());

        BlacklistItem dbBlacklist = null;

        LOG.debug(String.format("del black list[%d]\n", msgBlacklist.getId()));

        try {
            dbBlacklist = blacklistRepository.getById(msgBlacklist.getId());
        } catch (EntityNotFoundException enfe) {
            throw new EntityNotFoundException(String.format("BlackListItem with id #%d not found ", msgBlacklist.getId()));
        }


        LOG.debug("Deleting the blacklistitem");

        blacklistRepository.delete(dbBlacklist);

    }

    @Override
    @Transactional
    public Node getBlackListedItemNode(Set<Node> nodes) throws IPStringConversionException, IpTypeMissMatchException {
        Node out;
        out = blackListedItemNode(nodes);
        return out;
    }

    @Override
    @Transactional
    public List<BlacklistItem> createBlacklist(List<BlacklistItem> list) {
        Map<String, List<BlacklistItem>> map = blacklistRepository.getBlacklistItemsCidrHashMap(list);
        List<BlacklistItem> goodList = new ArrayList<BlacklistItem>();
        List<BlacklistItem> badList = new ArrayList<BlacklistItem>();
        List<BlacklistItem> blist;

        for (BlacklistItem item : list) {
            if (map.get(item.getCidrBlock()) != null) {
                blist = map.get(item.getCidrBlock());
            } else {
                blist = new ArrayList<BlacklistItem>();
            }
            if (blist.size() == 1) {
                for (BlacklistItem bli : blist) {
                    if (item.getBlacklistType() == null) {
                        blist.add(item);
                    } else if (item.getBlacklistType().equals(bli.getBlacklistType())) {
                        badList.add(item);
                    }
                    goodList.add(item);
                }
            } else if (blist.size() == 2) {
                badList.add(item);
            } else {
                if (item.getBlacklistType() == null) {
                    goodList.add(setBlacklistItemFields(item, BlacklistType.ACCESSLIST));
                    goodList.add(setBlacklistItemFields(item, BlacklistType.NODE));
                } else {
                    goodList.add(item);
                }
            }
        }

        if (badList.size() == 0) {
            blacklistRepository.saveBlacklist(goodList);
        }

        return badList;
    }

    private BlacklistItem setBlacklistItemFields(BlacklistItem item, BlacklistType type) {
        BlacklistItem blItem = new BlacklistItem();
        blItem.setBlacklistType(type);
        blItem.setCidrBlock(item.getCidrBlock());
        blItem.setIpVersion(item.getIpVersion());
        blItem.setUserName(item.getUserName());
        return blItem;
    }
}
