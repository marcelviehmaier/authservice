/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.jwt;

import de.hspf.auth.Account;
import de.hspf.auth.AuthController;
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
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Marcel
 */
@ApplicationScoped
public class JWTTokenProvider {
    
    private String key;

    @PostConstruct
    public void init() {
        key = readPemFile();
    }
    
    public String generateJWT(Account account) {
        JWTAuth provider = JWTAuth.create(null, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setSecretKey(key)
                ));

        MPJWTToken token = new MPJWTToken();
        token.setAud("targetService");
        token.setIss("http://localhost:8080");  // Must match the expected issues configuration values
        token.setJti(UUID.randomUUID().toString());

        token.setSub(account.getEmail());  // Sub is required for WildFly Swarm
        token.setUpn(account.getEmail());

        token.setIat(System.currentTimeMillis());
        token.setExp(System.currentTimeMillis() + 30000000); // 30 Seconds expiration!

        token.addAdditionalClaims("appl", "homeimprovement");
        token.addAdditionalClaims("username", account.getUsername());
        token.setGroups(Arrays.asList("user", "student", "protected"));
        token.setGroups(Arrays.asList("user", "student", "protected"));

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
