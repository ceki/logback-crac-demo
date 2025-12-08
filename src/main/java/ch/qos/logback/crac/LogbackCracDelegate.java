package ch.qos.logback.crac;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.model.Model;
import ch.qos.logback.core.model.ModelUtil;
import org.crac.Context;
import org.crac.Resource;

import static ch.qos.logback.core.CoreConstants.SAFE_JORAN_CONFIGURATION;

public class LogbackCracDelegate implements Resource {

    LoggerContext loggerContext;
    Model topModel;

    LogbackCracDelegate(LoggerContext loggerContext) {
        System.out.println("LogbackCracDelegate instantiated");
        this.loggerContext = loggerContext;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        long beforeStop = System.currentTimeMillis();
        this.topModel = (Model) loggerContext.getObject(SAFE_JORAN_CONFIGURATION);
        ModelUtil.resetForReuse(topModel);
        loggerContext.stop();
        long afterStop = System.currentTimeMillis();
        System.out.println("Logback check-pointing took " + (afterStop - beforeStop) + " ms");

    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("afterRestore called - Logback restoring from model...");
        long beforeProcessModel = System.currentTimeMillis();
        loggerContext.start();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        if(topModel != null) {
            configurator.processModel(topModel);
        } else {
            System.out.println("Warning: topModel is null in LogbackCracDelegate.afterRestore");
        }
        long afterProcessModel = System.currentTimeMillis();
        System.out.println("Logback restore from model took " + (afterProcessModel - beforeProcessModel) + " ms");
    }
}
