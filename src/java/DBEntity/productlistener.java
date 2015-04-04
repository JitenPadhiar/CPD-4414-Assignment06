/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBEntity;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 *
 * @author c0644689
 */
@MessageDriven(mappedName = "jms/Queue")
public class productlistener {

    @EJB
    productList productlist;

    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();

        } catch (JMSException ex) {
            Logger.getLogger(productlistener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
 