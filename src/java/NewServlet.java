/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Jiten
 */
@Path("/NewServlet")
public class NewServlet {

    @GET
    @Produces("application/json")
    public String doGet()
            throws SQLException {

        JSONArray jObject = new JSONArray();
        Connection conn = Connect.getConnection();
        String query = "SELECT * FROM product";

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int col = rs.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < col; i++) {
                String coln1 = rs.getMetaData().getColumnLabel(i + 1).toLowerCase();
                Object coln2 = rs.getObject(i + 1);
                obj.put(coln1, coln2);
            }
            jObject.add(obj);

        }
        return jObject.toJSONString();
    }

    private String getResults(String query, String... params) {

        StringBuilder sb = new StringBuilder();

        try (Connection conn = Connect.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }

            ResultSet rs = pstmt.executeQuery();
            JSONObject obj = new JSONObject();
            while (rs.next()) {

                obj.put("ProductID", rs.getInt("productId"));
                obj.put("Name", rs.getString("name"));
                obj.put("Description", rs.getString("description"));
                obj.put("Quantity", rs.getInt("quantity"));
                sb.append(obj.toJSONString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(NewServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    @POST
    @Path("{productId}")
    public void doPost(String st) {

        JSONObject obj = new JSONObject();
        String name = (String) obj.get("name");
        String quantity = (String) obj.get("quantity");
        String description = (String) obj.get("description");

        doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name, description, quantity);

    }

    @PUT
    @Path("{productId}")
    public void doPut(@PathParam("productId") int ProductID, String st) throws ParseException {

        JSONObject obj = (JSONObject) new JSONParser().parse(st);
        String id = (String) obj.get("productId");
        String name = (String) obj.get("name");
        String quantity = (String) obj.get("quantity");
        String description = (String) obj.get("description");

        Connection conn = Connect.getConnection();
        doUpdate("UPDATE product SET name=?,description=?,quantity=? where productId=? ", name, description, quantity, id);

    }

    @DELETE
    @Path("{productId}")
    public void doDelete(@PathParam("productId") int id) throws SQLException {

        Connection conn = Connect.getConnection();

        String query = "DELETE FROM product where productId=? " + id;
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.executeUpdate();
    }

    private void doUpdate(String query, String... params) {

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

    }

}
