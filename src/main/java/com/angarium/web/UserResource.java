package com.angarium.web;

import com.angarium.model.NewStandardUserModel;
import com.angarium.model.ResetUserModel;
import com.angarium.model.UserModel;
import com.angarium.service.UserService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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

    @POST
    @RunOnVirtualThread
    @RolesAllowed("admin")
    public UserModel createStandardUser(NewStandardUserModel newStandardUserModel){
        return userService.createStandardUser(newStandardUserModel);
    }

    @DELETE
    @Path("/{id}")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @POST
    @Path("/reset/{id}")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    public void resetUserPassword(Long id, ResetUserModel resetUserModel){
        userService.resetUserPassword(id, resetUserModel);
    }

}
