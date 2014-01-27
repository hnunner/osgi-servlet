/*
 * IpojoServletConfigured.java 23.10.2013 h.nunner
 */
package com.adviser.osgi.servlet.ipojo.configured;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * This example shows how to retrieve the HttpService via iPOJO and register the servlet in just a few steps.<br>
 * <br>
 * The instantiation of the servlet is done by adding IPojoServletConfigured-*.cfg files into [apache-karaf-x.x.x]/etc,
 * containing key value pairs of the annotated properties.
 *
 * @author h.nunner
 */
@SuppressWarnings("serial")
@Component(name = "IPojoServletConfigured")
public class IpojoServletConfigured extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(IpojoServletConfigured.class.getName());

    /** servlet alias via config file injection */
//    @Property(immutable = true, mandatory = true)
    private String servletAlias;
    /** greeting via config file injection */
//    @Property
    private String greeting;

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
        httpService.registerServlet(servletAlias, this, null, null);
        LOG.info(getClass().getName() + "::Registered servlet with alias '" + servletAlias + "'");
    }

    /**
     * Called on bundle shutdown.
     */
    @Invalidate
    private void stop() {
        httpService.unregister(servletAlias);
        LOG.info(getClass().getName() + "::Deregistered servlet with alias '" + servletAlias + "'");
    }


    /**
     * Update callback for servlet alias property.
     *
     * @param alias
     *          the new alias
     */
    @Property(name="servletAlias")
    public void updateAlias(String alias) {
        this.servletAlias = alias;
        LOG.info("new alias: " + alias);
    }

    /**
     * Update callback for greeting property.
     *
     * @param greeting
     *          the new greeting
     */
    @Property(name="greeting")
    public void updateGreeting(String greeting) {
        this.greeting = greeting;
        LOG.info("new greeting: " + greeting);
    }


    /**
     * Callback for general property changes.<br>
     * <br>
     * Invoked, when changes of any property in the corresponding cfg file occured.
     *
     * @param conf
     *          the new property configuration - this parameter is optional
     */
    @Updated
    private void updated(Dictionary<String, String> conf) {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append("::Property change recognized in servlet (").append(servletAlias).append("): ");
        Enumeration<String> keys = conf.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            sb.append(key).append("=").append(conf.get(key));
            if (keys.hasMoreElements()) {
                sb.append(", ");
            }
        }
        LOG.info(sb.toString());
    }


    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream out = res.getOutputStream();
        StringBuilder sb = new StringBuilder();
        sb.append(greeting).append(" iPOJO configured!");
        out.write(sb.toString().getBytes());
        out.close();
    }

}
