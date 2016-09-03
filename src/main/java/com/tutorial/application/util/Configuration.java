/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tutorial.application.util;

import java.io.Serializable;

/**
 *
 * @author marcos.mendonca
 */
public interface Configuration extends Serializable {

    static final String secret = "876623358d92a1cb941b23ab3a69742d"; //"welcomeGermany";

    static final String key = "blog";

    final Short expireMinutes = 20;
}
