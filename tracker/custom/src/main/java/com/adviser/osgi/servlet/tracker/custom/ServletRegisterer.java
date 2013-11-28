/*
 * ServletRegisterer.java 26.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.custom;

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

    /** servlet alias */
    private String servletAlias;
    /** servlet */
    private HttpServlet servlet;


    /**
     * Constructor.
     *
     * @param context
     *          bundle context
     * @param servletAlias
     *          servlet alias
     * @param servlet
     *          servlet
     */
    public ServletRegisterer(BundleContext context, String servletAlias, HttpServlet servlet) {
        super(context, HttpService.class.getName(), null);
        this.servletAlias = servletAlias;
        this.servlet = servlet;
    }


    /** {@inheritDoc} */
    @Override
    public Object addingService(ServiceReference reference) {
        HttpService service = (HttpService) context.getService(reference);
        try {
            service.registerServlet(servletAlias, servlet, null, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unable to register the servlet.", e);
        }
        LOG.log(Level.INFO, "Servlet successfully registered.");
        return service;
    }

    /** {@inheritDoc} */
    @Override
    public void removedService(ServiceReference reference, Object service) {
        try {
            ((HttpService) service).unregister(servletAlias);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Unable to unregister servlet.", e);
        }
        context.ungetService(reference);
        LOG.log(Level.INFO, "Servlet successfully unregistered.");
    }

}
