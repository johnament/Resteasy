package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.plugins.server.netty.RequestDispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;

/**
 * Created by John.Ament on 2/20/14.
 */
@Vetoed
public class NettyServerRunnable implements Runnable {
    @Override
    public void run() {
        ResteasyCdiExtension cdiExtension = CDI.current().select(ResteasyCdiExtension.class).get();
        NettyConfiguration nettyConfiguration = getNettyConfiguration();
        NettyJaxrsServer netty = new NettyJaxrsServer();
        ResteasyDeployment rd = new ResteasyDeployment();
        rd.setActualResourceClasses(cdiExtension.getResourceClasses());
        rd.setInjectorFactoryClass(CdiInjectorFactory.class.getName());
        rd.getActualProviderClasses().addAll(cdiExtension.getProviderClasses());
        RequestDispatcher requestDispatcher = new CdiRequestDispatcher((SynchronousDispatcher)rd.getDispatcher(),
                rd.getProviderFactory(), nettyConfiguration.getSecurityDomain());
        netty.setDeployment(rd);
        netty.setPort(nettyConfiguration.getPort());
        netty.setRootResourcePath(nettyConfiguration.getPath());
        netty.setRequestDispatcher(requestDispatcher);
        netty.start();
    }


    private static NettyConfiguration getNettyConfiguration() {
        NettyConfiguration defaultConfig = new DefaultNettyConfiguration();
        NettyConfiguration located = null;
        CDI cdi = CDI.current();
        final Instance<NettyConfiguration> nettyConfigurationInstance = cdi.select(NettyConfiguration.class);
        if(nettyConfigurationInstance.isAmbiguous() || nettyConfigurationInstance.isUnsatisfied())
        {
            located = defaultConfig;
        }
        else
        {
            located = nettyConfigurationInstance.get();
        }
        return located;
    }
}
