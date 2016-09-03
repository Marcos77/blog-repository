/**
 * This class will provide Comment service such as CRUD services (create,
 * update, delete read)
 *
 * @author marcos
 */
package com.tutorial.application.services;


import com.tutorial.application.entity.Category;
import com.tutorial.application.entity.Comment;
import com.tutorial.application.entity.Post;
import com.tutorial.application.repository.CategoryRepository;
import com.tutorial.application.repository.CommentRepository;
import com.tutorial.application.repository.PostRepository;
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
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;


@Named
@Path("blogApp/v1/api/{postId}/comment")
public class CommentServImpl implements Services<Comment> {

    @Autowired
    CommentRepository repository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthenticateService auth;

    @GET
    @Path("/findAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@PathParam("postId") Long postId, Comment comment) {
        return Response.status(Response.Status.OK).entity(repository.findAllByPost(postRepository.findOne(postId))).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        Comment com = repository.findOne(id);
        return Response.status(Response.Status.OK).entity(com).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response save(@PathParam("postId") Long postId, Comment comment) {

        Response response = validate(comment);
        if (response != null) {
            return response;
        }

        Post post = postRepository.findOne(postId);
        comment.setPost(post);
        repository.save(comment);

        return Response.status(Status.OK).entity(comment).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long idCom, Comment comment) {

        Response response = validate(comment);
        if (response != null) {
            return response;
        }

        Comment com = repository.findOne(idCom);
        com.setText(comment.getText());
        repository.save(com);
        return Response.status(Response.Status.OK).entity(com).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("postId") Long postId, @PathParam("id") Long id, @HeaderParam("token") String token) {

        String userKey = getUserKey(token);
        if (userKey == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Invalid token").build();
        }

        Post post = postRepository.findOne(postId);
        Category category = post.getCategory();

        if (!userKey.equalsIgnoreCase(category.getBlog().getOwner())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the owner can delete a comment").build();
        }
        repository.delete(id);
        return Response.status(Response.Status.OK).entity("Comment deleted successfully").build();
    }

    /**
     * It will validate the required attributes
     *
     * @param comment
     * @return
     */
    @Override
    public Response validate(Comment comment) {
        if (comment.getText() == null || comment.getText().trim().length() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Text is required").build();
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
