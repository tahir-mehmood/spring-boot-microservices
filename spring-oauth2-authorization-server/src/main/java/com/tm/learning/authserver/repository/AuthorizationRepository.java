package com.tm.learning.authserver.repository;

import com.tm.learning.authserver.entity.Authorizations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorizations, String> {
    Optional<Authorizations> findByState(String state);

    Optional<Authorizations> findByAuthorizationCodeValue(String authorizationCode);

    Optional<Authorizations> findByAccessTokenValue(String accessToken);

    Optional<Authorizations> findByRefreshTokenValue(String refreshToken);
//    Optional<Authorizations> findByOidcIdTokenValue(String idToken);
//    Optional<Authorizations> findByUserCodeValue(String userCode);
//    Optional<Authorizations> findByDeviceCodeValue(String deviceCode);


    @Query("select a from Authorizations a where a.state = :token" +
            " or a.authorizationCodeValue = :token" +
            " or a.accessTokenValue = :token" +
            " or a.refreshTokenValue = :token"
//            "+ or a.oidcIdTokenValue = :token" +
//            " or a.userCodeValue = :token" +
//            " or a.deviceCodeValue = :token"
)
    Optional<Authorizations> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(@Param("token") String token);
}
