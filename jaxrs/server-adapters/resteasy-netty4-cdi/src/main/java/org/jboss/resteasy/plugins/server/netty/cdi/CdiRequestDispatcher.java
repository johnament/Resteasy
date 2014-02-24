package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.netty.RequestDispatcher;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.weld.context.bound.BoundRequestContext;

import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A RequestDispatcher that initiates a Weld RequestContext at the start of processing, and releases it at the end.
 */
@Vetoed
public class CdiRequestDispatcher extends RequestDispatcher {
    public CdiRequestDispatcher(SynchronousDispatcher dispatcher, ResteasyProviderFactory providerFactory, SecurityDomain domain) {
        super(dispatcher, providerFactory, domain);
    }

    @Override
    public void service(HttpRequest request, HttpResponse response, boolean handleNotFound) throws IOException {
        final BoundRequestContext requestContext = CDI.current().select(BoundRequestContext.class).get();
        Map<String,Object> requestMap = new HashMap<String,Object>();
        requestContext.associate(requestMap);
        requestContext.activate();
        try {
            super.service(request,response,handleNotFound);
        }
        finally {
            requestContext.invalidate();
            requestContext.deactivate();
            requestContext.dissociate(requestMap);
        }
    }
}
