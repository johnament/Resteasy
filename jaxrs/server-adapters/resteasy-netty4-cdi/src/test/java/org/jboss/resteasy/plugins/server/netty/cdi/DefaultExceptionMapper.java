package org.jboss.resteasy.plugins.server.netty.cdi;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * A default exception mapper impl.
 *
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        return Response.ok(exception.getMessage()).build();
    }
}
