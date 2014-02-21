package org.jboss.resteasy.plugins.server.netty.cdi;

import org.jboss.weld.environment.se.Weld;

import javax.enterprise.inject.Vetoed;

/**
 * Created by John.Ament on 2/20/14.
 */
@Vetoed
public class CdiNettyBoot {
    public static void main(String[] args) {
        Weld weld = new Weld();
        weld.initialize();
        Runnable runnable = new NettyServerRunnable();
        Thread runner = new Thread(runnable);
        runner.setDaemon(false);
        runner.start();
    }
}
