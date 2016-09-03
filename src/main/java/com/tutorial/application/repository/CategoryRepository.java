/*
 * This is CategoryRepository it extends JpaRepository from
 * String data framework and it is used to provide all 
 * basic database queries and responsable for database transation
 */

package com.tutorial.application.repository;

import com.tutorial.application.entity.Blog;
import com.tutorial.application.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marcos
 */
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    /**
     * This will bring all Category from a specific Blog
     * @param blog
     * @return 
     */
    List<Category> findAllByBlog(Blog blog);
}
