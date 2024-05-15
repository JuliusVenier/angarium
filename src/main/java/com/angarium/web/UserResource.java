package com.angarium.web;

import com.angarium.model.UserModel;
import com.angarium.service.UserService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/api/user")
public class UserResource {
    private final UserService userService;

    @GET
    @Path("/whoami")
    @RunOnVirtualThread
    @RolesAllowed({"user", "admin"})
    public UserModel whoami(@Context SecurityContext securityContext){
        return userService.findByUsername(securityContext.getUserPrincipal().getName());
    }
}
