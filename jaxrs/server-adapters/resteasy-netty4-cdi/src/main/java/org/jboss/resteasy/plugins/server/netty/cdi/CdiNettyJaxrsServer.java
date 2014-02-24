package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.plugins.server.netty.RequestDispatcher;

import javax.enterprise.inject.Vetoed;

/**
 * A simple extension of the NettyJaxrsServer that uses a CDI enabled request dispatcher.  Each
 */
@Vetoed
public class CdiNettyJaxrsServer extends NettyJaxrsServer {
    @Override
    public RequestDispatcher createRequestDispatcher() {
        return new CdiRequestDispatcher((SynchronousDispatcher)deployment.getDispatcher(),
                deployment.getProviderFactory(), domain);
    }
}
