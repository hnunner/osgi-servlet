osgi-servlet
============
Purpose
-------

On the one hand this project shows different ways to implement **servlets** in an OSGi environment. On the other hand it shows different ways of **retrieving OSGi services** from the service registry.  

The project is implemented using Maven and consists of several subprojects, with every subproject being a selfcontained example implementation.

Branches might show different ways of implementing a concrete feature.

**CAUTION**: Not all of the examples are meant to be used in the way they are implemented here. A critical look is needed for every example, deciding which approach could be useful for a concrete implementation and which example is not useful at all.  


Todos before deployment (using Apache Karaf)
--------------------------------------------
create config file "[karaf_home]/etc/org.ops4j.pax.web.cfg", with the following content

    org.osgi.service.http.port=8080  
    org.apache.felix.http.jettyEnabled=true
