/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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


    public List<product> productList = new ArrayList<product>();

    public productList() {

        product product = new product();

        try (Connection conn = Connect.getConnection()) {
            String query = "SELECT * FROM product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                product.productId = rs.getInt("productId");
                product.name = rs.getString("name");
                product.description = rs.getString("description");
                product.quantity = rs.getInt("quantity");

                productList.add(product);

            }

        } catch (SQLException ex) {
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void remove(product product) throws Exception {

        remove(product.productId);
    }

    public void remove(int id) throws Exception {

        int value = doUpdate("DELETE FROM product where productId=? ", String.valueOf(id));
        if (value > 0) {
            product dt = getNew(id);
            productList.remove(dt);

        } else {
            throw new Exception("Error");
        }

    }

    public void add(product product) throws Exception {

        int value = doUpdate("INSERT INTO product (productId, name,description,quantity) VALUES (?,?,?,?)", String.valueOf(product.productId), product.name, product.description, String.valueOf(product.quantity));

        if (value > 0) {
            productList.add(product);

        } else {
            throw new Exception("Error");
        }
    }

    public void set(int id, product product) throws Exception {
        int data = doUpdate("UPDATE product SET name=?,description=?,quantity=? where productId=?", product.name, product.description, String.valueOf(product.quantity), String.valueOf(product.productId));

        if (data > 0) {
            product dt = getNew(id);
            dt.setName(product.name);
            dt.setDescription(product.getDescription());
            dt.setQuantity(product.getQuantity());

        } else {
            throw new Exception("Error");
        }
    }

    public JsonArray toJSON() {

        JsonArray obj = null;

        for (product product : productList) {
            JsonObject json = Json.createObjectBuilder()
                    .add("productId", product.productId)
                    .add("name", product.name)
                    .add("description", product.description)
                    .add("quantity", product.quantity)
                    .build();
            obj.add(json);
        }

        return obj;
    }

    private int doUpdate(String query, String... params) {

        int str = 0;
        try (Connection conn = Connect.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(NewServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return str;
    }

    private product getNew(int id) {
        product value = null;
        for (product product : productList) {
            if (product.getProductID() == id) {
                value = product;
            }
        }

        return value;
    }
}
