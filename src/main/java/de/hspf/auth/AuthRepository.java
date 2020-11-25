/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.auth;

import java.util.ArrayList;
import java.util.List;
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
    
}
