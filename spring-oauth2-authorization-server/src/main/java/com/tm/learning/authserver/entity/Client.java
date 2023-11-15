package com.tm.learning.authserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

/**
 * Client 정보를 등록하는 Entity
 */
@Entity
@Data
public class Client {

    // PK
    @Id
    private String id;


    @Column(unique = true)
    private String clientId;


    private String clientSecret;


    private Instant clientIdIssuedAt;


    private Instant clientSecretExpiresAt;


    private String clientName;


    @Column(length = 1000)
    private String clientAuthenticationMethods;

    @Column(length = 1000)
    private String authorizationGrantTypes;


    @Column(length = 1000)
    private String redirectUris;


    @Column(length = 1000)
    private String postLogoutRedirectUris;


    @Column(length = 1000)
    private String scopes;


    @Column(length = 2000)
    private String clientSettings;


    @Column(length = 2000)
    private String tokenSettings;
}
