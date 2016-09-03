/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tutorial.application.services.authentication;

/**
 *
 * @author marcos.mendonca_bs
 */
public interface AuthenticateService {
    
    boolean validateAuth(String token);
    
    String getUser(String token);
}
