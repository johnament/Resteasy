package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;

import javax.enterprise.inject.Vetoed;

/**
 * Created by John.Ament on 2/20/14.
 */
@Vetoed
public class DefaultNettyConfiguration implements NettyConfiguration {
    @Override
    public int getPort() {
        return 8080;
    }

    @Override
    public SecurityDomain getSecurityDomain() {
        return null;
    }

    @Override
    public String getPath() {
        return "/";
    }
}
