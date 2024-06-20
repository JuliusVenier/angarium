package com.angarium.web;

import com.angarium.model.NewStandardUserModel;
import com.angarium.model.UpdateUserModel;
import com.angarium.model.UserModel;
import com.angarium.service.UserService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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
    @Path("/me/whoami")
    @RunOnVirtualThread
    @RolesAllowed({"user", "admin"})
    public UserModel whoami(@Context SecurityContext securityContext){
        return userService.findByUsername(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Path("/me/update")
    @RunOnVirtualThread
    @RolesAllowed({"admin", "user"})
    public UserModel updateUser(UpdateUserModel updateUserModel){
        return userService.updateUser(updateUserModel);
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
    public UserModel createStandardUser(@Valid NewStandardUserModel newStandardUserModel){
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
    public UserModel resetUserPassword(Long id){
        return userService.resetUserPassword(id);
    }

}
