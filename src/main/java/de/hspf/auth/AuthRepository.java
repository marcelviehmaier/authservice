/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.auth;

import de.hspf.authservice.secure.MPJWTToken;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Marcel
 */
@ApplicationScoped
public class AuthRepository {
   
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private List<Account> accountList = new ArrayList<Account>();
    
    public Account safeUser(Account account){
        logger.log(Level.INFO, "Save user: {0}", account.getUsername());
        this.accountList.add(account);
        return account;
    }
    
    public boolean userExists(Account signUpAccount){
        return this.accountList.stream().anyMatch((account) -> (signUpAccount.getEmail().equals(account.getEmail())));
    }
    
    public Account getAccountByEmail(Account loginAccount){
        for(Account account : this.accountList){
            if(loginAccount.getEmail().equals(account.getEmail())){
                return account;
            }
        }
        return null;
    }
    
}
