/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBEntity;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author c0644689
 */
@MessageDriven(mappedName = "jms/Queue")
public class productlistener implements MessageListener {

    @Inject
    productList productlist;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String str = ((TextMessage) message).getText();
                JsonObject obj = Json.createReader(new StringReader(str)).readObject();
                productlist.add(new product(obj));

            }
        } catch (JMSException ex) {
            Logger.getLogger(productlistener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(productlistener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
