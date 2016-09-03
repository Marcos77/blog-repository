/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tutorial.application.services.authentication;

import com.tutorial.application.util.Configuration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.DatatypeConverter;


/**
 *This class provide authentication services
 * @author marcos.mendonca_bs
 */
@Named
@Path("authentication")
public class AuthenticateServImpl implements AuthenticateService {
    

    /**
     * It will authenticate the owner and provide a valid token
     * @param owner
     * @param password
     * @return 
     */
    @GET
    @Path("/auth")
    public String authenticate(@QueryParam("owner") String owner, @QueryParam("password") String password) {
        
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(password.getBytes(),0,password.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Configuration.secret.equals(password) && owner != null && owner.trim().length()>0) {
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, Configuration.expireMinutes);
            
            String compactJws = Jwts.builder()
             .setSubject(owner)
             .setExpiration(cal.getTime())
            .signWith(SignatureAlgorithm.HS512, Configuration.key)
            .compact();

            return compactJws;
        }
        
        return null;
    }
    
   /**
    * It will validate if the token is validate (date of expiration)
    * @param token
    * @return 
    */
    @Override
     public boolean validateAuth(String token) {
        
        try {
            Claims claims = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(Configuration.key))
            .parseClaimsJws(token).getBody();

            System.out.println("ID: " + claims.getId());
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issuer: " + claims.getIssuer());
            System.out.println("Expiration: " + claims.getExpiration());

            if (claims.getExpiration().before(new Date())) {
                System.out.println("Token has expired");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    /**
     * It will provide the user name only if the token is still valid
     * @param token
     * @return 
     */
    @Override
    public String getUser(String token) {
       if (!validateAuth(token)) {
           return null;
       }
        Claims claims = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(Configuration.key))
            .parseClaimsJws(token).getBody();
        
       return claims.getSubject();
   }
     
}
