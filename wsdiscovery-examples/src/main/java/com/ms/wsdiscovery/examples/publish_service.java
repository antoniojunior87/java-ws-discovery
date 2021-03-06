/*
publish_service.java

Copyright (C) 2008-2009 Magnus Skjegstad

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.ms.wsdiscovery.examples;

import com.ms.wsdiscovery.WsDiscoveryConstants;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.exception.WsDiscoveryXMLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import com.ms.wsdiscovery.WsDiscoveryFactory;
import com.ms.wsdiscovery.WsDiscoveryServer;
import com.ms.wsdiscovery.datatypes.WsDiscoveryScopesType;
import com.ms.wsdiscovery.exception.WsDiscoveryNetworkException;
import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import com.ms.wsdiscovery.servicedirectory.exception.WsDiscoveryServiceDirectoryException;
import java.util.logging.Level;

/**
 * How to publish a Web Service with WS-Discovery. See also the example
 * publish_jaxws.java.
 * 
 * @author Magnus Skjegstad
 */
public class publish_service {
    public static void main(String[] argv) 
            throws WsDiscoveryServiceDirectoryException, 
            WsDiscoveryNetworkException, InterruptedException, WsDiscoveryXMLException, WsDiscoveryException {

        // Set the logger level
        //WsDiscoveryConstants.loggerLevel = Level.FINEST;

        // Create WS-Discovery server instance
        WsDiscoveryServer server = WsDiscoveryFactory.createServer();
        
        System.out.println("Starting WS-Discovery server...");
        server.start();

        // Create a dummy service using WsDiscoverBuilder
        WsDiscoveryService service1 = 
                WsDiscoveryFactory.createService(
                    new QName("namespace", "myTestService"), // Port type.
                    "http://myscope",                            // Scope.
                    "http://localhost:1234/myTestService");  // Invocation address (XAddrs)
        
        // Register the service in the local service directory        
        System.out.println("Publishing service:\n" + service1.toString());
        server.publish(service1);
        
        // Create another dummy service using the WsDiscoveryService constructor. The 
        // constructor offers a wider range of options when creating a new service - 
        // for instance multiple port types and manually created endpoint references.
        
        // Create a list of port types
        List<QName> ports = new ArrayList<QName>();
        ports.add(new QName("namespace", "myOtherTestService_type1"));
        ports.add(new QName("namespace", "myOtherTestService_type2"));
        ports.add(new QName("namespace", "myOtherTestService_type3"));
        
        // And several scopes..
        WsDiscoveryScopesType scopes = new WsDiscoveryScopesType();
        scopes.getValue().add("http://myscope");
        scopes.getValue().add("http://other_scope");        
        
        // And multiple invocation addresses
        List<String> invocationAddresses = new ArrayList<String>();
        invocationAddresses.add("http://localhost:1234");
        invocationAddresses.add("http://10.10.10.1:4321/MyTestService");
        
        // Create service 
        WsDiscoveryService service2 = 
                new WsDiscoveryService(
                        ports,
                        scopes,
                        invocationAddresses);
                                        
        // Register the service in the local service directory        
        System.out.println("Publishing service:\n" + service2.toString());
        server.publish(service2);
                        
        // Main loop. 
        System.out.println("Running....");
        while (server.isRunning())
            synchronized (server) {
                server.wait();
            }
    }
}
