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
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

/**
 * Diese Klasse stellt verschiedene Endpunkte zur Verwaltung von Benutzern bereit.
 */
@Slf4j
@RequiredArgsConstructor
@Path("/user")
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
    @Operation(summary = "Gibt das Modell des aktuellen Benutzers zurück",
            description = "Diese Methode gibt das UserModel des aktuell angemeldeten Benutzers zurück.")
    public UserModel whoami(@Context SecurityContext securityContext){
        return userService.findByUsername(securityContext.getUserPrincipal().getName());
    }

    /**
     * POST-Ressource, um die Informationen des aktuellen Benutzers zu aktualisieren.
     *
     * @param updateUserModel Das Modell mit den neuen Benutzerinformationen.
     * @return Das aktualisierte UserModel.
     */
    @POST
    @Path("/me/update")
    @RunOnVirtualThread
    @RolesAllowed({"admin", "user"})
    @Operation(summary = "Aktualisiert die Informationen des aktuellen Benutzers",
            description = "Diese Methode aktualisiert die Informationen des aktuell angemeldeten Benutzers basierend auf den angegebenen Daten.")
    public UserModel updateUser(UpdateUserModel updateUserModel){
        return userService.updateUser(updateUserModel);
    }

    /**
     * GET-Ressource, die eine Liste aller Benutzer zurückgibt.
     *
     * @return Eine Liste von UserModel-Objekten.
     */
    @GET
    @Path("/all")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    @Operation(summary = "Listet alle Benutzer auf",
            description = "Diese Methode gibt eine Liste aller Benutzer im System zurück.")
    public List<UserModel> listAllUsers() {
        return userService.listAllUsers();
    }

    /**
     * POST-Ressource, um einen neuen Standardbenutzer zu erstellen.
     *
     * @param newStandardUserModel Das Modell mit den Informationen des neuen Benutzers.
     * @return Das erstellte UserModel.
     */
    @POST
    @RunOnVirtualThread
    @RolesAllowed("admin")
    @Operation(summary = "Erstellt einen neuen Standardbenutzer",
            description = "Diese Methode erstellt einen neuen Standardbenutzer basierend auf den angegebenen Daten.")
    public UserModel createStandardUser(@Valid NewStandardUserModel newStandardUserModel){
        return userService.createStandardUser(newStandardUserModel);
    }

    /**
     * DELETE-Ressource, um einen Benutzer zu löschen.
     *
     * @param id Die ID des zu löschenden Benutzers.
     */
    @DELETE
    @Path("/{id}")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    @Operation(summary = "Löscht einen Benutzer",
            description = "Diese Methode löscht einen Benutzer basierend auf der angegebenen ID.")
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    /**
     * POST-Ressource, um das Passwort eines Benutzers zurückzusetzen.
     *
     * @param id Die ID des Benutzers, dessen Passwort zurückgesetzt werden soll.
     * @return Das aktualisierte UserModel.
     */
    @POST
    @Path("/reset/{id}")
    @RunOnVirtualThread
    @RolesAllowed("admin")
    @Operation(summary = "Setzt das Passwort eines Benutzers zurück",
            description = "Diese Methode setzt das Passwort eines Benutzers basierend auf der angegebenen ID zurück.")
    public UserModel resetUserPassword(Long id){
        return userService.resetUserPassword(id);
    }

}
