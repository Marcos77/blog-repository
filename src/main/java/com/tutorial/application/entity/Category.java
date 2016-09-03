/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tutorial.application.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marcos
 */
@Entity
@Table(name="category", schema="blogdb")
@SequenceGenerator(name = "catSeq", initialValue = 1, allocationSize = 1, schema = "blogdb", sequenceName = "blogdb.cat_id_seq")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonSerialize
public class Category implements Serializable {
    
    @Id
    @Column(name="ID_CAT")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "catSeq")
    private Long id;
    
    @Column(name="NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="ID_BLOG", referencedColumnName = "ID_BLOG")
    private Blog blog;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
    
    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> postList;

  
    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
    
    
}
