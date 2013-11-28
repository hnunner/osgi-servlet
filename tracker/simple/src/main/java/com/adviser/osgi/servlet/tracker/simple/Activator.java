/*
 * Activator.java 25.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.simple;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * This example uses a ServiceTracker to get the HttpService. The disadvantage is, that the service is required exactly
 * on bundle startup. It also does not recognize, when the HttpService gets lost.
 *
 * @author h.nunner
 */
public class Activator implements BundleActivator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());
    /** servlet alias */
    private static final String SERVLET_ALIAS = "/simpletracker";

    /** http service tracker */
    private ServiceTracker httpTracker;


    /** {@inheritDoc} */
    @Override
    public void start(BundleContext context) throws Exception {
        httpTracker = new ServiceTracker(context, HttpService.class.getName(), null);
        httpTracker.open();

        HttpService service = (HttpService) httpTracker.waitForService(5000);
        if (service == null) {
            throw new Exception("No HttpService found. Unable to register ContextServlet.");
        }

        service.registerServlet(SERVLET_ALIAS, new SimpleTrackerServlet(), null, null);
        LOG.info(getClass().getName() + "::Start");
    }

    /** {@inheritDoc} */
    @Override
    public void stop(BundleContext context) throws Exception {
        HttpService service = (HttpService) httpTracker.waitForService(5000);
        if (service == null) {
            LOG.log(Level.WARNING, "No HttpService found. Unable to register ContextServlet.");
            return;
        }

        try {
            // XXX will throw an exception, if servlet has not been registered before
            service.unregister(SERVLET_ALIAS);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Unable to unregister ContextServlet.", e);
        }

        httpTracker.close();
        LOG.info(getClass().getName() + "::Stop");
    }

}
