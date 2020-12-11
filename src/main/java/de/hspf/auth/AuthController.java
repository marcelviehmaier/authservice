package de.hspf.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response.Status;

@Path("/auth")
@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/signup")
    @Transactional
    public Response postSignUp(Account account) {
        String response = this.authService.handleSignUp(account);
        if(response.equals("SUCCESS")){
            return Response.ok(account).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }
    
    @POST
    @Path("/login")
    public Response postLogin(Account account) {
        String token = this.authService.handleLogin(account);
        if(token.equals("null")){
            Response.status(Status.CONFLICT).build();
        }
        return Response.ok(token).build();
    }
    
}
