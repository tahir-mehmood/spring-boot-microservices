package com.tm.learning.authserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

/**
 * 인증된 사용자에게 반환한 권한에 대한 정보를 저장하는 Entity
 */
@Entity
@Data
public class Authorizations {

    // PK
    @Id
    private String id;


    private String registeredClientId;


    private String principalName;


    private String authorizationGrantType;

    // scope
    @Column(length = 1000)
    private String authorizedScopes;


    @Column(columnDefinition = "TEXT")
    private String attributes;


    @Column(length = 500)
    private String state;


    @Column(columnDefinition = "NVARCHAR(255)")
    private String authorizationCodeValue;


    private Instant authorizationCodeIssuedAt;


    private Instant authorizationCodeExpiresAt;


    @Column(columnDefinition = "NVARCHAR(255)")
    private String authorizationCodeMetadata;

    // Access Token
    @Column(columnDefinition = "TEXT")
    private String accessTokenValue;


    private Instant accessTokenIssuedAt;


    private Instant accessTokenExpiresAt;


    @Column(columnDefinition = "TEXT")
    private String accessTokenMetadata;


    private String accessTokenType;


    @Column(columnDefinition = "NVARCHAR(255)")
    private String accessTokenScopes;

    // Refresh Token
    @Column(columnDefinition = "TEXT")
    private String refreshTokenValue;


    private Instant refreshTokenIssuedAt;


    private Instant refreshTokenExpiresAt;


    @Column(columnDefinition = "NVARCHAR(255)")
    private String refreshTokenMetadata;


//    // OpenId Connect ID Token
//    @Column(columnDefinition="NVARCHAR(255)")
//    private String oidcIdTokenValue;

//    private Instant oidcIdTokenIssuedAt;
//
//    // OpenId Connect Id Token
//    private Instant oidcIdTokenExpiresAt;
//
//    // OpenId Connect Id Token
//    @Column(columnDefinition="NVARCHAR(255)")
//    private String oidcIdTokenMetadata;
//
//    // OpenId Connect Id Token
//    @Column(columnDefinition="NVARCHAR(255)")
//    private String oidcIdTokenClaims;

//    @Column(length = 4000)
//    private String userCodeValue;
//    private Instant userCodeIssuedAt;
//    private Instant userCodeExpiresAt;
//    @Column(length = 2000)
//    private String userCodeMetadata;
//
//    @Column(length = 4000)
//    private String deviceCodeValue;
//    private Instant deviceCodeIssuedAt;
//    private Instant deviceCodeExpiresAt;
//    @Column(length = 2000)
//    private String deviceCodeMetadata;
}
