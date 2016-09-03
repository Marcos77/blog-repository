/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tutorial.application.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marcos
 */
@Entity
@Table(name="blog", schema="blogdb")
@SequenceGenerator(name = "blogSeq", initialValue = 1, allocationSize = 1, schema = "blogdb", sequenceName = "blogdb.blog_id_seq")
public class Blog implements Serializable {
    
    @Id
    @Column(name="ID_BLOG")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "blogSeq")
    private Long id;
    
    @Column(name="OWNER")
    private String owner;
    
    @Column(name="NAME")
    private String name;

    @OneToMany(mappedBy = "blog", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Category> cateroryList;
    
    public Blog(Long id, String owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
    }

    public Blog() {
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Blog{" + "id=" + id + ", owner=" + owner + ", name=" + name + '}';
    }
    
    
    
}
