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
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    private String key;

    @PostConstruct
    public void init() {
        key = readPemFile();
    }
    
    public Account handleSignUp(Account account){
        if(authRepository.userExists(account)){
            this.logger.log(Level.INFO, "User with username {0} already exists", account.getUsername());
            return account;
        }
        this.logger.info("Register new user");
        return this.authRepository.safeUser(account);
    }
    
    public String handleLogin(Account account){
        if(account.getPassword().equals(this.authRepository.getAccountByEmail(account).getPassword())){
            this.logger.info("Correct credentials. Create JWT.");
            return this.generateJWT(key);
        }
        this.logger.info("Not able to login user. Maybe wrong credentials?");
        return null;
    }
    
    private String generateJWT(String key) {
        JWTAuth provider = JWTAuth.create(null, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setSecretKey(key)
                ));

        MPJWTToken token = new MPJWTToken();
        token.setAud("targetService");
        token.setIss("https://server.example.com");  // Must match the expected issues configuration values
        token.setJti(UUID.randomUUID().toString());

        token.setSub("Jessie");  // Sub is required for WildFly Swarm
        token.setUpn("Jessie");

        token.setIat(System.currentTimeMillis());
        token.setExp(System.currentTimeMillis() + 30000); // 30 Seconds expiration!

        token.addAdditionalClaims("custom-value", "Jessie specific value");

        token.setGroups(Arrays.asList("user", "protected"));

        return provider.generateToken(new JsonObject().mergeIn(token.toJSONString()), new JWTOptions().setAlgorithm("RS256"));
    }

    // NOTE:   Expected format is PKCS#8 (BEGIN PRIVATE KEY) NOT PKCS#1 (BEGIN RSA PRIVATE KEY)
    // See gencerts.sh
    private static String readPemFile() {
        StringBuilder sb = new StringBuilder(8192);
        try (BufferedReader is = new BufferedReader(
                new InputStreamReader(
                        AuthController.class.getResourceAsStream("/privateKey.pem"), StandardCharsets.US_ASCII))) {
            String line;
            while ((line = is.readLine()) != null) {
                if (!line.startsWith("-")) {
                    sb.append(line);
                    sb.append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
