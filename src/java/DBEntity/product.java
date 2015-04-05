/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBEntity;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author c0644689
 */
public class product {
    
    public int productId;
    public String name;
    public String description;
    public int quantity;

    
    public product(){
        
    }

    public product(int productID, String name, String description, int quantity) {
        this.productId = productID;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }
    
    public product(JsonObject obj ){
        
        this.productId = obj.getInt("productId");
        this.name = obj.getString("name");
        this.description = obj.getString("description");
        this.quantity = obj.getInt("quantity");
        
    }
    
    public JsonObject toJSON(){
        JsonObject obj = Json.createObjectBuilder()
                .add("productID",this.productId)
                .add("name",this.name)
                .add("descrition",this.description)
                .add("quantity",this.quantity)
                .build();
        
        return obj;
        
    }
    
    public int getProductID() {
        return productId;
    }

    public void setProductID(int productID) {
        this.productId = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

  
    
}
