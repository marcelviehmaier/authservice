/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.auth;

import de.hspf.jwt.JWTTokenProvider;
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
    JWTTokenProvider tokenProvider;
    @Inject
    AuthRepository authRepository;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public String handleSignUp(Account account){
        if(authRepository.userExists(account)){
            this.logger.log(Level.INFO, "User with username {0} already exists", account.getUsername());
            return "FAILED";
        }
        this.logger.info("Register new user");
        this.authRepository.safeUser(account);
        return "SUCCESS";
    }
    
    public String handleLogin(Account account){
        if(account.getPassword().equals(this.authRepository.getAccountByEmail(account).getPassword())){
            this.logger.info("Correct credentials. Create JWT...");
            String token = this.tokenProvider.generateJWT(account);
            return token;
        }
        this.logger.info("Not able to login user. Maybe wrong credentials?");
        return null;
    }

}
