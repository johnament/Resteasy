package org.jboss.resteasy.plugins.server.netty.cdi;

import javax.enterprise.inject.Vetoed;

/**
 * Created by John.Ament on 2/20/14.
 */
@Vetoed
public class CdiNettyBoot {
    public static void main(String[] args) {
        Runnable runnable = new NettyServerRunnable();
        Thread runner = new Thread(runnable);
        runner.setDaemon(false);
        runner.start();
    }
}
