/*
 * Activator.java 25.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.custom;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This example shows how to access the HttpService via a custom ServiceTracker (ServletRegisterer). The component listens
 * for the HttpService to be added to the service registry and registers the servlet, when it comes up.
 *
 * @author h.nunner
 */
public class Activator implements BundleActivator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());
    /** servlet alias */
    private static final String SERVLET_ALIAS = "/customtracker";

    /** servlet registerer */
    private ServletRegisterer registerer;


    /** {@inheritDoc} */
    @Override
    public void start(BundleContext context) throws Exception {
        registerer = new ServletRegisterer(context, SERVLET_ALIAS, new CustomTrackerServlet());
        registerer.open();

        LOG.info(getClass().getName() + "::Start");
    }

    /** {@inheritDoc} */
    @Override
    public void stop(BundleContext context) throws Exception {
        registerer.close();

        LOG.info(getClass().getName() + "::Stop");
    }

}
