/*
 * This is BlogRepository it extends JpaRepository from
 * String data framework and it is used to provide all 
 * basic database queries and responsable for database transation
 */
package com.tutorial.application.repository;

import com.tutorial.application.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marcos
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>{
    
    /*
    * It override findOne from spring data
    * Also, it uses the Id property from Blog Entity
    */
    Blog findById(Long id);
    
}
