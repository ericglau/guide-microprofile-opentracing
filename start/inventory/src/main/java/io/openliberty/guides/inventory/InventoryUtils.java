// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.inventory;

import java.net.URL;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import javax.ws.rs.ProcessingException;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.client.UnknownUrlException;
import io.openliberty.guides.inventory.client.UnknownUrlExceptionMapper;

public class InventoryUtils {

  private final String SYSTEM_PORT = System.getProperty("system.http.port");
  
    public Properties getProperties(String hostname) {
        try {
            String customUrlString = "http://" + hostname + ":" + SYSTEM_PORT + "/system";
            URL customURL = new URL(customUrlString);
            SystemClient customRestClient = RestClientBuilder.newBuilder()
                                                             .baseUrl(customURL)
                                                             .register(UnknownUrlExceptionMapper.class)
                                                             .build(SystemClient.class);
            return customRestClient.getProperties();
        } catch (ProcessingException ex) {
            handleProcessingException(ex);
        } catch (UnknownUrlException ex) {
            System.err.println("The given URL is unreachable.");
        } catch (MalformedURLException ex) {
            System.err.println("The given URL is not formatted correctly: " + ex.getMessage());
        }
        return null;
    }

    public void handleProcessingException(ProcessingException ex) {
        Throwable rootEx = ExceptionUtils.getRootCause(ex);
        if (rootEx != null && rootEx instanceof UnknownHostException) {
            System.err.println("The specified host is unknown: " + ex.getMessage());
        } else {
            throw ex;
        }
    }
}
