/**
 * This class will provide Category service such as CRUD services (create,
 * update, delete read)
 *
 * @author marcos
 */
package com.tutorial.application.services;

import com.tutorial.application.entity.Blog;
import com.tutorial.application.entity.Category;
import com.tutorial.application.repository.BlogRepository;
import com.tutorial.application.repository.CategoryRepository;
import com.tutorial.application.services.authentication.AuthenticateService;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("blogApp/v1/api/{idBlog}/category")
public class CategoryServImpl implements Services<Category> {

    @Autowired
    CategoryRepository repository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    AuthenticateService auth;

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@PathParam("idBlog") Long idBlog) {
        Blog blog = blogRepository.findOne(idBlog);
        return Response.status(Response.Status.OK).entity(repository.findAllByBlog(blog)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(repository.findOne(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@PathParam("idBlog") Long idBlog, Category category, @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Response response = validate(category);
        if (response != null) {
            return response;
        }

        Blog blog = blogRepository.findById(idBlog);
        if (!userKey.equalsIgnoreCase(blog.getOwner())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can create a category").build();
        }

        category.setBlog(blog);
        repository.save(category);

        return Response.status(Response.Status.OK).entity(category).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("idBlog") Long idBlog, @PathParam("id") Long idCat, Category category, @HeaderParam("token") String token) {
        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }
        Response response = validate(category);
        if (response != null) {
            return response;
        }
        Blog blog = blogRepository.findById(idBlog);
        if (!userKey.equalsIgnoreCase(blog.getOwner())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can update a category").build();
        }

        Category categoryLoad = repository.findOne(idCat);
        categoryLoad.setBlog(blog);
        categoryLoad.setName(category.getName());
        repository.save(categoryLoad);
        return Response.status(Response.Status.OK).entity(categoryLoad).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("idBlog") Long idBlog, @PathParam("id") Long id, @HeaderParam("token") String token) {
        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Blog blog = blogRepository.findById(idBlog);
        if (!userKey.equalsIgnoreCase(blog.getOwner())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can delete a category").build();
        }

        repository.delete(id);
        return Response.status(Response.Status.OK).entity("Category deleted successfully").build();
    }

    /**
     * It will validate the required attributes
     *
     * @param category
     * @return
     */
    @Override
    public Response validate(Category category) {
        if (category.getName() == null || category.getName().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name is required").build();
        }
        return null;
    }

    /**
     * Gets the user key based on token from Authentication Service
     *
     * @param token
     * @return
     */
    private String getUserKey(String token) {
        return auth.getUser(token);
    }

}
