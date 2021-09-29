package ru.job4j.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.store.HbmStore;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HbmStore.instOf();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            HbmStore.instOf().close();
        } catch (Exception e) {
            LOG.error("Hibernate destruction error", e);
        }
    }
}
