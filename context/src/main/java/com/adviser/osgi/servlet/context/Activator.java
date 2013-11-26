/*
 * ContextServletActivator.java 25.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.context;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

/**
 * @author h.nunner
 */
public class Activator implements BundleActivator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());
    /** servlet alias */
    private static final String SERVLET_ALIAS = "/context";


    /** {@inheritDoc} */
    @Override
    public void start(BundleContext context) throws Exception {
        // XXX service already registered at this point?
        ServiceReference reference = context.getServiceReference(HttpService.class.getName());
        if (reference == null) {
            throw new Exception("No service reference found for HttpService. Unable to register ContextServlet.");
        }
        HttpService service = (HttpService) context.getService(reference);
        if (service == null) {
            throw new Exception("No HttpService found. Unable to register ContextServlet.");
        }

        service.registerServlet(SERVLET_ALIAS, new ContextServlet(), null, null);
        LOG.info(getClass().getName() + "::Start");
    }

    /** {@inheritDoc} */
    @Override
    public void stop(BundleContext context) throws Exception {
        ServiceReference reference = context.getServiceReference(HttpService.class.getName());
        if (reference == null) {
            LOG.log(Level.WARNING, "No service reference found for HttpService. Unable to unregister ContextServlet.");
            return;
        }
        HttpService service = (HttpService) context.getService(reference);
        if (service == null) {
            LOG.log(Level.WARNING, "No HttpService found. Unable to unregister ContextServlet.");
            return;
        }

        try {
            // XXX will throw an exception, if servlet has not been registered before
            service.unregister(SERVLET_ALIAS);
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Unable to unregister ContextServlet.", e);
        }

        LOG.info(getClass().getName() + "::Stop");
    }

}
