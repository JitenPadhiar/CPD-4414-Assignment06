/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author Jiten
 */
@Path("/NewServlet")
public class NewServlet {

    @GET
    @Produces("application/json")

    public Response doGet() {

        return Response.ok(getResults("SELECT * FROM product")).build();

    }

    @GET
    @Path("{id}")

    public Response getNew(@PathParam("id") String id) {

        return Response.ok(getResults("SELECT * FROM product WHERE productId=?", id)).build();
    }

    private String getResults(String query, String... params) {

        StringBuilder sb = new StringBuilder();
        JsonObject obj = null;
        try (Connection conn = Connect.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                obj = Json.createObjectBuilder()
                        .add("ProductID", rs.getInt("productId"))
                        .add("Name", rs.getString("name"))
                        .add("Description", rs.getString("description"))
                        .add("Quantity", rs.getInt("quantity"))
                        .build();

                sb.append(obj.toString());

            }

        } catch (SQLException ex) {
            Logger.getLogger(NewServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")

    public Response doPost(JsonObject obj) {

        //JSONObject obj = new JSONObject();
        String name = obj.getString("name");
        String quantity = String.valueOf(obj.getInt("quantity"));
        String description = obj.getString("description");

        doUpdate("INSERT INTO product (name,description,quantity) VALUES (?,?,?)", name, description, quantity);

        return Response.ok(obj).build();

    }

    @PUT
    @Path("{productId}")
    public Response doPut(@PathParam("productId") String id, JsonObject obj) {

       // JSONObject obj = (JSONObject) new JSONParser().parse(st);
        //  id =  obj.getString("productId");
        String name = obj.getString("name");
        String quantity = String.valueOf(obj.getInt("quantity"));
        String description = obj.getString("description");

        Connection conn = Connect.getConnection();
        doUpdate("UPDATE product SET name=?,description=?,quantity=? where productId=? ", name, description, quantity, id);

        return Response.ok(obj).build();

    }

    @DELETE
    @Path("{productId}")
    public Response doDelete(@PathParam("productId") String id) {

        doUpdate("DELETE FROM product WHERE productId=? ", id);

        return Response.ok().build();

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
