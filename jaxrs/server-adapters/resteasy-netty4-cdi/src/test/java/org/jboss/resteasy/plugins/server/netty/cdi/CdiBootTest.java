package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.ClientBuilder;

/**
 * A test to verify that the server starts up and able to generate a constraint violation.
 */
@RunWith(Arquillian.class)
public class CdiBootTest {
    @Deployment
    public static JavaArchive createArchive() {
        String beans = "<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\"\n" +
                "       version=\"1.1\" bean-discovery-mode=\"all\">\n" +
                "</beans>\n";
        return ShrinkWrap.create(JavaArchive.class, CdiBootTest.class.getSimpleName() + ".jar")
                .addPackage(CdiRequestDispatcher.class.getPackage())
                .addClasses(EchoResource.class, DefaultExceptionMapper.class)
                .addAsManifestResource(new StringAsset("org.jboss.resteasy.cdi.ResteasyCdiExtension"),
                        "services/javax.enterprise.inject.spi.Extension")
                .addAsManifestResource(new StringAsset(beans),"beans.xml");
    }

    private CdiNettyJaxrsServer server;
    private int port;

    @Before
    public void setup() {
        server = new CdiNettyJaxrsServer();
        port = (int)(Math.random()*9000)+ 1000;
        ResteasyCdiExtension cdiExtension = CDI.current().select(ResteasyCdiExtension.class).get();
        ResteasyDeployment rd = new ResteasyDeployment();
        rd.setActualResourceClasses(cdiExtension.getResourceClasses());
        rd.setInjectorFactoryClass(CdiInjectorFactory.class.getName());
        rd.getActualProviderClasses().addAll(cdiExtension.getProviderClasses());
        server.setDeployment(rd);
        server.setPort(port);
        server.setRootResourcePath("/api");
        server.start();
    }

    @After
    public void shutdown() {
        this.server.stop();
    }

    @Test
    public void testClientFailure() throws InterruptedException {
        String expected = "value was null";
        String value = ClientBuilder.newClient().target("http://localhost:"+port).path("/api/echo")
                .queryParam("name", "null").request().buildGet().invoke(String.class);
        Assert.assertTrue(value.contains(expected));
    }

    @Test
    public void testClientSuccess() {
        String expected = "Hello, Bob!";
        String value = ClientBuilder.newClient().target("http://localhost:"+port).path("/api/echo")
                .queryParam("name", "Bob").request().buildGet().invoke(String.class);
        Assert.assertEquals(expected,value);
    }

}
