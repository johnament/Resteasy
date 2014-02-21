package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;

/**
 * Configuration for using Netty.
 *
 * Created by John.Ament on 2/20/14.
 */
public interface NettyConfiguration {
    /**
     * The port to listen on.  If not available, uses port 8080
     *
     * @return
     */
    public int getPort();

    /**
     * The security domain for this application.
     * @return
     */
    public SecurityDomain getSecurityDomain();

    /**
     * The path.  If not present, uses /
     *
     * @return
     */
    public String getPath();
}
