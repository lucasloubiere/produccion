/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lloubiere
 */
@ManagedBean
@SessionScoped
public class TestWS {

    /**
     * Creates a new instance of TestWS
     */
    public TestWS() {
    }

    public void test() {
        
        try {

            Client client = Client.create();
            
            client.addFilter(new HTTPBasicAuthFilter("sistemastock", "n8a49s6v17e416f4c"));

            WebResource webResource = client.resource("http://192.168.15.55:8080/sgc/rest/secure/detalleBalanza/fecha/02012016/codigoPlanta/0002/empresa/1");

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("Output from Server .... \n");
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}


