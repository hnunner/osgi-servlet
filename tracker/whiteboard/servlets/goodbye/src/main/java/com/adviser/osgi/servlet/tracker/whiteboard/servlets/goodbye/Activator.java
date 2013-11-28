/*
 * Activator.java 26.11.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.whiteboard.servlets.goodbye;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author h.nunner
 */
public class Activator implements BundleActivator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Activator.class.getName());
    /** servlet alias */
    private static final String SERVLET_ALIAS = "/whiteboard/goodbye";


    /** {@inheritDoc} */
    @Override
    public void start(BundleContext context) throws Exception {
        GoodbyeServlet servlet = new GoodbyeServlet();
        Hashtable<String, String> props   = new Hashtable<String, String>();
        props.put("alias",  SERVLET_ALIAS);
        context.registerService(HttpServlet.class.getName(), servlet, props);
        LOG.info(getClass().getName() + "::Start");
    }

    /** {@inheritDoc} */
    @Override
    public void stop(BundleContext context) throws Exception {
        LOG.info(getClass().getName() + "::Stop");
    }
}
