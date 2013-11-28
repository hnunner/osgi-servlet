/*
 * Activator.java 25.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.whiteboard.servletregisterer;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This example shows how to deploy a servlet inspired by the whiteboard approach described by Knoplerfish
 * (http://www.knopflerfish.org/osgi_service_tutorial.html). It uses a ServiceTracker listening for Servlets to be
 * registered in the service registry and registering the servlet at the HttpService, if available. So instead of
 * registering the servlet itself (other tracker examples), it uses a global registerer registering every servlet found
 * in the registry (Inversion of Control).
 *
 * @author h.nunner
 */
public class Activator implements BundleActivator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());

    /** servlet registerer */
    private ServletRegisterer registerer;


    /** {@inheritDoc} */
    @Override
    public void start(BundleContext context) throws Exception {
        registerer = new ServletRegisterer(context);
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
