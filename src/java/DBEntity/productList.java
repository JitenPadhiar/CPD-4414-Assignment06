/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import service.Connect;
import service.NewServlet;

/**
 *
 * @author c0644689
 */

@Singleton

public class productList {
    
    @EJB
    NewServlet service;
  
    public List<product> productList;
    
     public productList() {
        
        product product = new product();

        try (Connection conn = Connect.getConnection()) {
            String query = "SELECT * FROM product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                product.productID = rs.getInt("productID");
                product.name = rs.getString("name");
                product.description = rs.getString("description");
                product.quantity = rs.getInt("quantity");

                productList.add(product);

            }

        } catch (SQLException ex) {
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void add(product product) {

        service.doPost(product.toJSON());
        productList.add(product);

    }

    public void remove(product product) {

        int id = product.productID;
        service.doDelete(String.valueOf(id));
        productList.remove(product);

    }

    public void remove(int id) {

        service.doDelete(String.valueOf(id));

    }

    public void set(int id, product product) {
        service.doPut(String.valueOf(id), product.toJSON());

    }

    public JsonArray toJSON(){
        
        JsonArray obj = null;
        
        for(product product : productList){
             JsonObject json = Json.createObjectBuilder()
                    .add("productId", product.productID)
                    .add("name", product.name)
                    .add("description",  product.description)
                    .add("quantity", product.quantity)
                    .build();
         obj.add(json);
        }
        
        return obj;
    }
    
}
