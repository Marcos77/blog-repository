/**
 * This class will provide Blog service
 * such as CRUD services (create, update, delete read)
 * @author marcos
 */
package com.tutorial.application.services;

import com.tutorial.application.entity.Blog;
import com.tutorial.application.repository.BlogRepository;
import com.tutorial.application.services.authentication.AuthenticateService;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;


@Named
@Path("blogApp/v1/api/blog")
public class BlogServImpl implements Services<Blog> {

    @Autowired
    BlogRepository repository;

    @Autowired
    AuthenticateService auth;

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(Blog blog) {
        return Response.status(Response.Status.OK).entity(repository.findAll()).build();

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(repository.findById(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(Blog blog, @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Response response = validate(blog);
        if (response != null) {
            return response;
        }

        blog.setOwner(userKey);
        repository.save(blog);

        return Response.status(Response.Status.OK).entity(blog).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Blog blog, @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Response response = validate(blog);
        if (response != null) {
            return response;
        }
        Blog blogLoaded = repository.findOne(id);
        blogLoaded.setName(blog.getName());
        repository.save(blogLoaded);
        return Response.status(Response.Status.OK).entity(blogLoaded).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id, @HeaderParam("token") String token) {
        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Blog blog = repository.findOne(id);
        if (!blog.getOwner().equalsIgnoreCase(userKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can delete a post").build();
        }

        repository.delete(id);
        return Response.status(Response.Status.OK).entity("Blog deleted successfully").build();
    }

    /**
     * It will validate the required attributes
     * @param blog
     * @return 
     */
    @Override
    public Response validate(Blog blog) {
        if (blog.getName() == null || blog.getName().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name of the blog is required").build();
        }

        if (blog.getOwner() == null || blog.getOwner().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("The Owner of the blog is required").build();
        }
        return null;
    }

    /**
     * Gets the user key based on token from Authentication Service
     * @param token
     * @return 
     */
    private String getUserKey(String token) {
        return auth.getUser(token);
    }

}
