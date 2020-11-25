package de.hspf.auth;

import de.hspf.authservice.secure.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.POST;

@Path("/auth")
@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/signup")
    public Response postSignUp(Account account) {
        return Response.ok(this.authService.handleSignUp(account)).build();
    }
    
    @POST
    @Path("/login")
    public Response postLogin(Account account) {
        return Response.ok(this.authService.handleLogin(account)).build();
    }
    
}
