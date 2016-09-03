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
@Table(name="comment", schema="blogdb")
@SequenceGenerator(name = "comSeq", initialValue = 1, allocationSize = 1, schema = "blogdb", sequenceName = "blogdb.com_id_seq")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonSerialize
public class Comment implements Serializable {
    
    @Id
    @Column(name="ID_COM")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "comSeq")
    private Long id;
    
    @Column(name="TEXT")
    private String text;
    
    @ManyToOne
    @JoinColumn(name = "ID_POST", referencedColumnName = "ID_POST")
    private Post post;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    
    
}
