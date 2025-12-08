package ch.qos.logback.crac;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;
import org.crac.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackCracDemo {


    public static void main(String[] args) throws JoranException {
        System.out.println("Crac Logback integration test");


        long time0 = System.currentTimeMillis();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        long time1 = System.currentTimeMillis();
        System.out.println("Logback configuration took " + (time1 - time0) + " ms");


        System.out.println("Registering LogbackCracDelegate with CRaC...");
        LogbackCracDelegate logbackCracDelegate = new LogbackCracDelegate(context);
        Core.getGlobalContext().register(logbackCracDelegate);

        // ---------------------------------------------------------------
        int i = 0;
        Logger logger = context.getLogger(LogbackCracDemo.class);
        while(i++ < 2000) {
            logger.info("Crac Logback integration test logging..." + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
