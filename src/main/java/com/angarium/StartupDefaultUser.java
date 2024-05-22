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
public class StartupDefaultUser {

    @ConfigProperty(name = "angarium.default.name")
    String defaultName;

    @ConfigProperty(name = "angarium.default.password")
    String defaultPassword;

    private final UserService userService;

    /**
     * Initialisiert den Deafult-Benutzer beim Systemstart.
     *
     *
     * @param evt StartupEvent-Objekt, das den Systemstart signalisiert.
     */
    public void initializeAdminUser(@Observes StartupEvent evt) {
        log.info("Initializing Default User");

        if (userService.userExists(defaultName)) {
            log.info("Default user found");
            return;
        }
        log.info("Default user not found");
        createAdminUser();

        log.info("Default user initialized");
    }

    /**
     * Erstellt den Default-Benutzer, falls er noch nicht existiert.
     */
    private void createAdminUser() {
        log.info("Creating Default User");
        userService.createUser(new NewUserModel(defaultName, defaultPassword, "user"));
    }
}
