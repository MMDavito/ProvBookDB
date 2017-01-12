/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.services;

import javafx.scene.media.Media;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.beans.BookBean;

/**
 *
 * @author daca97002
 */
@Path("/")
public class BookService {

    @EJB
    BookBean bookbean;

    @POST
    @Path("book")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(String body) {
        if (bookbean.addBook(body)) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        JsonArray data = bookbean.getBooks();
        if (data == null) {
            return Response.serverError().build();
        }
        return Response.ok(data).build();
    }

    @DELETE
    @Path("book/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        if (bookbean.deleteBook(id)) {
            return Response.status(Response.Status.ACCEPTED).build();

        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("book")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(String body) {
        if(bookbean.updateBook(body)){
            return Response.status(Response.Status.CREATED).build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @GET
    @Path("authors")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors() {
        JsonArray data = bookbean.getAuthors();
        if (data == null) {
            return Response.serverError().build();
        }
        return Response.ok(data).build();
    }
    
}
