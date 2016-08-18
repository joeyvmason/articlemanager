package com.joeyvmason.articlemanager.core.domain.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.joeyvmason.articlemanager.core.domain.AuditableEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Document
public class User extends AuditableEntity {

    private String emailAddress;
    private String zipCode;
    private String firstName;
    private String lastName;

    @JsonIgnore
    private byte[] shaPassword;

    private Set<Role> roles = Sets.newHashSet();

    public User() {}

    public User(String emailAddress, String firstName, String lastName, String zipCode, String password) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        setPassword(password);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress != null) {
            emailAddress = emailAddress.toLowerCase().trim();
        }

        this.emailAddress = emailAddress;
    }

    @JsonIgnore
    public byte[] getShaPassword() {
        return shaPassword;
    }

    public void setShaPassword(byte[] shaPassword) {
        this.shaPassword = shaPassword;
    }

    public void setPassword(String password) {
        this.shaPassword = getSha512(password);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public void clearRoles() {
        this.roles = Sets.newHashSet();
    }

    public enum Role {
        ADMIN
    }
}
