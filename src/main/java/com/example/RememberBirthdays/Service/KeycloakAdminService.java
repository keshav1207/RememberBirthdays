package com.example.RememberBirthdays.Service;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class KeycloakAdminService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakAdminService(Keycloak keycloak){
        this.keycloak = keycloak;
    }

    public String createUserInkeycloak(String firstName, String lastName, String email, String password ){
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(email);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            return location.substring(location.lastIndexOf('/') + 1); // Extract Keycloak userId
        } else {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }
    }

    public void deleteUserFromKeycloak(String keycloakUserId) {
        keycloak.realm(realm).users().get(keycloakUserId).remove();
    }

    public void updateUserInKeycloak(String keycloakUserId, String firstName, String lastName, String email) {
        UserRepresentation user = keycloak.realm(realm).users().get(keycloakUserId).toRepresentation();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(email);

        keycloak.realm(realm).users().get(keycloakUserId).update(user);
    }

    


}
