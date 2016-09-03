/*
 * This is CommentRepository it extends JpaRepository from
 * String data framework and it is used to provide all 
 * basic database queries and responsable for database transation
 */

package com.tutorial.application.repository;

import com.tutorial.application.entity.Comment;
import com.tutorial.application.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marcos.mendonca_bs
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{
 
    /**
     * It will find all Comments based on a specific Post
     * @param post
     * @return 
     */
    List<Comment> findAllByPost(Post post);
}
