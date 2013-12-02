/*
 * HelloWorldServlet.java 23.10.2013 h.nunner
 */
package com.adviser.osgi.servlet.ipojo;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * This example shows how to retrieve the HttpService via iPOJO and register the servlet in just a few steps.
 *
 * @author h.nunner
 */
@SuppressWarnings("serial")
@Component(immediate = true)
@Instantiate
public class IpojoServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(IpojoServlet.class.getName());
    private static final String SERVLET_ALIAS = "/ipojo";

    /** http service injection */
    @Requires
    private HttpService httpService;


    /**
     * Called on bundle startup.
     *
     * @throws NamespaceException
     *            if the registration fails because the alias is already in use
     * @throws ServletException
     *            if the servlet's init method throws an exception,
     *            or the given servlet object has already been registered at a different alias
     */
    @Validate
    private void start() throws ServletException, NamespaceException, Exception {
        httpService.registerServlet(SERVLET_ALIAS, this, null, null);
        LOG.info(getClass().getName() + "::Start");
    }

    /**
     * Called on bundle shutdown.
     */
    @Invalidate
    private void stop() {
        httpService.unregister(SERVLET_ALIAS);
        LOG.info(getClass().getName() + "::Stop");
    }


    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream out = res.getOutputStream();
        out.write("Hello iPOJO!".getBytes());
        out.close();
    }

}
