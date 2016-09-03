/*
 * This is PostRepository it extends JpaRepository from
 * String data framework and it is used to provide all 
 * basic database queries and responsable for database transation
 */

package com.tutorial.application.repository;

import com.tutorial.application.entity.Category;
import com.tutorial.application.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marcos.mendonca_bs
 */
public interface PostRepository extends JpaRepository<Post, Long>{
    
    /**
     * It will find all Post based on a specific Category
     * @param category
     * @return 
     */
    List<Post> findAllBycategory(Category category);
}
