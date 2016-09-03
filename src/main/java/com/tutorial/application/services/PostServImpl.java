/**
 * This class will provide Post service such as CRUD services (create, update,
 * delete read)
 *
 * @author marcos
 */
package com.tutorial.application.services;

import com.tutorial.application.entity.Blog;
import com.tutorial.application.entity.Category;
import com.tutorial.application.entity.Post;
import com.tutorial.application.repository.CategoryRepository;
import com.tutorial.application.repository.PostRepository;
import com.tutorial.application.services.authentication.AuthenticateService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.ws.rs.Consumes;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

@Named
@Path("blogApp/v1/api/{idCat}/post")
public class PostServImpl implements Services<Post> {

    @Autowired
    PostRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthenticateService auth;

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@PathParam("idCat") Long idCat) {
        return Response.status(Response.Status.OK).entity(repository.findAllBycategory(categoryRepository.findOne(idCat))).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(repository.findOne(id)).build();
    }

    @PUT
    @Path("/{idPost}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            @PathParam("idCat") Long idCat,
            @PathParam("idPost") Long id,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Post post = repository.findOne(id);

        Response response = validate(post);
        if (response != null) {
            return response;
        }

        Category category = categoryRepository.findOne(idCat);
        Blog blog = category.getBlog();

        if (!blog.getOwner().equalsIgnoreCase(userKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can upload a file").build();
        }
        post.setCategory(category);
        writeToFile(post, uploadedInputStream);
        repository.save(post);
        return Response.status(200).entity(post).build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@PathParam("idCat") Long idCat, Post post, @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Response response = validate(post);
        if (response != null) {
            return response;
        }

        Category category = categoryRepository.findOne(idCat);
        Blog blog = category.getBlog();
        post.setCategory(category);
        if (!blog.getOwner().equalsIgnoreCase(userKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can create a post").build();
        }
        repository.save(post);

        return Response.status(200).entity(post).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("idCat") Long idCat, @PathParam("id") Long id, Post post,
            @HeaderParam("token") String token
    ) {
        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Response response = validate(post);
        if (response != null) {
            return response;
        }

        Category category = categoryRepository.findOne(idCat);
        Blog blog = category.getBlog();
        if (!blog.getOwner().equalsIgnoreCase(userKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can update a post").build();
        }

        Post postLoaded = repository.findOne(id);
        postLoaded.setText(post.getText());
        postLoaded.setTitle(post.getTitle());
        postLoaded.setCategory(category);
        repository.save(postLoaded);
        return Response.status(200).entity(postLoaded).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("idCat") Long idCat, @PathParam("id") Long id, @HeaderParam("token") String token
    ) {
        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Post post = repository.getOne(id);
        Category category = categoryRepository.findOne(idCat);
        Blog blog = category.getBlog();
        if (!blog.getOwner().equalsIgnoreCase(userKey)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can delete a post").build();
        }

        repository.delete(id);
        return Response.status(Response.Status.OK).entity("Post deleted successfully").build();
    }

    /**
     * It will validate the required attributes
     *
     * @param post
     * @return
     */
    @Override
    public Response validate(Post post) {
        if (post.getText() == null || post.getText().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Text is required").build();
        }

        if (post.getTitle() == null || post.getTitle().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Title is required").build();
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

    /**
     * This method will get the InputStream and write into Post.file attribute
     *
     * @param post
     * @param uploadedInputStream
     */
    private void writeToFile(Post post, InputStream uploadedInputStream) {

        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                buffer.write(bytes, 0, read);
            }
            buffer.flush();

            post.setFile(buffer.toByteArray());

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
