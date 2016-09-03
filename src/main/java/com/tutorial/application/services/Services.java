/*
 * This interface is an idea of what can a Service have to provide
 * 
 */
package com.tutorial.application.services;

import java.io.Serializable;
import javax.ws.rs.core.Response;

/**
 *
 * @author marcos
 * @param <T>
 */
public interface Services<T extends Serializable> {

    /**
     * Validates require Entity attributes
     * @param t
     * @return
     * @throws Exception 
     */
        Response validate(T t) throws Exception;
        
        

    
}
