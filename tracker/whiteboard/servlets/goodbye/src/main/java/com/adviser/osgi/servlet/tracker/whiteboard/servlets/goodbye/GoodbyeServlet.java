/*
 * GoodbyeServlet.java 26.10.2013 h.nunner
 */
package com.adviser.osgi.servlet.tracker.whiteboard.servlets.goodbye;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author h.nunner
 */
@SuppressWarnings("serial")
public class GoodbyeServlet extends HttpServlet {

    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream out = res.getOutputStream();
        out.write("Goodbye Whiteboard!".getBytes());
        out.close();
    }

}
