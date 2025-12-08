/*
 * Logback: the reliable, generic, fast and flexible logging framework.
 *  Copyright (C) 1999-2025, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *     or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */

package ch.qos.logback.crac;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;
import org.crac.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo class for testing CRAC integration with Logback.
 * This class demonstrates how to register the LogbackCracDelegate and perform logging.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class LogbackCracDemo {

    /**
     * Main method to run the CRAC Logback integration test.
     * @param args command line arguments
     * @throws JoranException if there's an error in Joran configuration
     */
    public static void main(String[] args) throws JoranException {
        System.out.println("Crac Logback integration test");


        long time0 = System.currentTimeMillis();
        // Next call reads logback.xml configuration file
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
