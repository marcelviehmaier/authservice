/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.auth;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Marcel
 */
@ApplicationScoped
public class AuthService {
    
    @Inject
    AuthRepository authRepository;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public Account handleSignUp(Account account){
        if(authRepository.userExists(account)){
            this.logger.log(Level.INFO, "User with username {0} already exists", account.getUsername());
            return account;
        }
        this.logger.info("Register new user");
        this.authRepository.safeUser(account);
        return account;
    }
}
