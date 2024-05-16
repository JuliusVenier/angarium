package com.angarium.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Diese Klasse repräsentiert einen Benutzer in der Anwendung.
 * Sie wird mit JPA persistiert und verwendet, um Benutzer in der Datenbank zu speichern und abzurufen.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "angarium_user")
@UserDefinition
public class UserEntity extends PanacheEntity{

    /**
     * Eindeutiger Benutzername des Benutzers.
     */
    @Column(unique = true)
    @Username
    private String username;

    /**
     * Passwort des Benutzers. (Wird in der Regel verschlüsselt gespeichert)
     */
    @Password
    private String password;

    /**
     * Rolle des Benutzers.
     */
    @Roles
    private String role;
}
