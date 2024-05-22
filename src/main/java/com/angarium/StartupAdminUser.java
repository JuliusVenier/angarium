package com.angarium;

import com.angarium.model.NewUserModel;
import com.angarium.service.UserService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
@RequiredArgsConstructor
@JBossLog
public class StartupAdminUser {

    @ConfigProperty(name = "angarium.admin.name")
    String adminName;

    @ConfigProperty(name = "angarium.admin.password")
    String adminPassword;

    private final UserService userService;

    /**
     * Initialisiert den Admin-Benutzer beim Systemstart.
     *
     *
     * @param evt StartupEvent-Objekt, das den Systemstart signalisiert.
     */
    public void initializeAdminUser(@Observes StartupEvent evt) {
        log.info("Initializing Admin User");

        if (userService.userExists(adminName)) {
            log.info("Admin user found");
            return;
        }
        log.info("Admin user not found");
        createAdminUser();

        log.info("Admin user initialized");
    }

    /**
     * Erstellt den Admin-Benutzer, falls er noch nicht existiert.
     */
    private void createAdminUser() {
        log.info("Creating Admin User");
        userService.createUser(new NewUserModel(adminName, adminPassword, "admin"));
    }
}