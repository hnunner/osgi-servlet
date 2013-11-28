/*
 * ServletRegisterer.java 26.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.whiteboard.servletregisterer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author h.nunner
 */
public class ServletRegisterer extends ServiceTracker {

    /** logger */
    private static final Logger LOG = Logger.getLogger(ServletRegisterer.class.getName());

    /** http tracker */
    private ServiceTracker httpTracker;


    /**
     * Constructor.
     *
     * @param context
     *          bundle context
     */
    public ServletRegisterer(BundleContext context) {
        super(context, HttpServlet.class.getName(), null);
        httpTracker = new ServiceTracker(context, HttpService.class.getName(), null);
        httpTracker.open();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object addingService(ServiceReference reference) {
        HttpServlet servlet = (HttpServlet) context.getService(reference);
        String alias = (String) reference.getProperty("alias");
        try {
            getHttpService().registerServlet(alias, servlet, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to register the servlet.", e);
        }
        LOG.log(Level.INFO, "Servlet successfully registered.");

        return servlet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removedService(ServiceReference reference, Object service) {
        String alias = (String) reference.getProperty("alias");
        try {
            getHttpService().unregister(alias);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unable to unregister the servlet.", e);
        }
        context.ungetService(reference);
        LOG.log(Level.INFO, "Servlet successfully unregistered.");
    }

    /**
     * @return the HttpService
     */
    private HttpService getHttpService() {
        HttpService httpService = null;
        try {
            httpService = (HttpService) httpTracker.waitForService(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupt by another thread.", e);
        }

        if (httpService == null) {
            throw new IllegalStateException("HttpService is not available.");
        }
        return httpService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        super.close();
        httpTracker.close();
    }

}
