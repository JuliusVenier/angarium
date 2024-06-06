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

import java.util.List;

@RequiredArgsConstructor
@Path("/api/user")
public class UserResource {

    private final UserService userService;

    /**
     * GET-Ressource, die das aktuelle angemeldete Benutzermodell zurückgibt.
     *
     * @param securityContext Der Sicherheitskontext, der Informationen über den aktuellen Benutzer enthält.
     * @return Das UserModel des aktuell angemeldeten Benutzers.
     */
    @GET
    @Path("/whoami")
    @RunOnVirtualThread
    @RolesAllowed({"user", "admin"})
    public UserModel whoami(@Context SecurityContext securityContext){
        return userService.findByUsername(securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("/all")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    public List<UserModel> listAllUsers() {
        return userService.listAllUsers();
    }
}
