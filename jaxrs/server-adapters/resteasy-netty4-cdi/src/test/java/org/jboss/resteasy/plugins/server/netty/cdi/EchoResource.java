package org.jboss.resteasy.plugins.server.netty.cdi;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("/echo")
public class EchoResource {

    @GET
    @Produces("text/plain")
    public String echo(@QueryParam("name") String name) {
        if(name.equals("null")) {
            throw new NullPointerException("value was null");
        }
        return String.format("Hello, %s!",name);
    }
}
