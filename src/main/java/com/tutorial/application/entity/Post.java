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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marcos
 */

@Entity
@Table(name="post", schema="blogdb")
@SequenceGenerator(name = "postSeq", initialValue = 1, allocationSize = 1, schema = "blogdb", sequenceName = "blogdb.post_id_seq")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonSerialize

public class Post implements Serializable {
    
    @Id
    @Column(name="ID_POST")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "postSeq")
    private Long id;
    
    @Column(name = "TITLE")
    private String title;
    
    @Column(name="TEXT")
    private String text;
    
    @Column(name="FILE", length=100000)
    private byte[] file;
    
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_CAT", referencedColumnName = "ID_CAT")
                
    Category category;

    public Post() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    
    
}
