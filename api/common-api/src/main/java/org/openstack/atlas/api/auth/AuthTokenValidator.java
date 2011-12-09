package org.openstack.atlas.api.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openstack.atlas.api.config.PublicApiServiceConfigurationKeys;
import org.openstack.atlas.api.exceptions.MissingFieldException;
import org.openstack.atlas.api.filters.helpers.StringUtilities;
import org.openstack.atlas.cfg.Configuration;
import org.openstack.client.keystone.KeyStoneAdminClient;
import org.openstack.client.keystone.KeyStoneException;
import org.openstack.client.keystone.token.FullToken;
import org.openstack.client.keystone.user.User;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class AuthTokenValidator {
    private static final Log LOG = LogFactory.getLog(AuthTokenValidator.class);

    public KeyStoneAdminClient keyStoneAdminClient;
    private Configuration configuration;

    public AuthTokenValidator(Configuration cfg) throws MalformedURLException, URISyntaxException, KeyStoneException {
        this.configuration = cfg;
        if (cfg.hasKeys(PublicApiServiceConfigurationKeys.auth_management_uri,
                PublicApiServiceConfigurationKeys.basic_auth_user,
                PublicApiServiceConfigurationKeys.basic_auth_key)) {

            LOG.info("Auth URI from local conf: " + configuration.getString(PublicApiServiceConfigurationKeys.auth_management_uri));
            keyStoneAdminClient = new KeyStoneAdminClient(configuration.getString(PublicApiServiceConfigurationKeys.auth_management_uri),
                    configuration.getString(PublicApiServiceConfigurationKeys.basic_auth_key),
                    configuration.getString(PublicApiServiceConfigurationKeys.basic_auth_user));
        } else {
            LOG.error(StringUtilities.AUTH_INIT_FAIL);
            throw new MissingFieldException(StringUtilities.AUTH_INIT_FAIL);
        }
    }

    public FullToken validate(Integer passedAccountId, String authToken) throws KeyStoneException, URISyntaxException {
        LOG.info("Within validate ... about to call client authenticate...");
        //Validating mosso style user...
        return keyStoneAdminClient.validateToken(String.valueOf(passedAccountId), authToken, "mosso");
    }
}