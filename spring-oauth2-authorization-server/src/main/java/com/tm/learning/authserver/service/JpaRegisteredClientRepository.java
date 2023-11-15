package com.tm.learning.authserver.service;

import com.tm.learning.authserver.entity.Client;
import com.tm.learning.authserver.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2 서버에서 클라이언트의 등록된 정보를 검색하고 제공<br>
 * RegisteredClient는 Spring Security에서 OAuth 2.0 및 OpenID Connect 클라이언트를 나타내는 모델 클래스
 * @implement : RegisteredClientRepository
 */
@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {


    private final ClientRepository clientRepository;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public JpaRegisteredClientRepository(ClientRepository clientRepository) {
        Assert.notNull(clientRepository, "clientRepository cannot be null");
        this.clientRepository = clientRepository;


        ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);

            this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    /**
     * RegisteredClient를 Client Entity로 변환하여 DB에 저장하는 메소드
     * @param registeredClient the {@link RegisteredClient}
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.clientRepository.save(toEntity(registeredClient));
    }

    /**
     * Client를 DB에서 ID로 조회하고, RegisteredClient 객체로 변환해서 전달하는 메소드
     * @param id the registration identifier
     * @return RegisteredClient
     */
    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.clientRepository.findById(id).map(this::toObject).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    /**
     * Client를 RegisteredClient 객체로 변환하는 메소드
     * @param Client
     * @return RegisteredClient
     */
    private RegisteredClient toObject(Client client) {
        Set<String> clientAuthenticationMethods =
                StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());

        Set<String> authorizationGrantTypes =
                StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());

        Set<String> redirectUris =
                StringUtils.commaDelimitedListToSet(client.getRedirectUris());
        Set<String> postLogoutRedirectUris =
                StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris());

        Set<String> clientScopes =
                StringUtils.commaDelimitedListToSet(client.getScopes());


        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())

                 .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))

                 .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))

                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));


        if(client.getClientSettings() != null) {
            Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
            builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());
        }


        if(client.getTokenSettings() != null) {
            Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
            builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());
        }

        return builder.build();
    }

    /**
     * RegisteredClient 객체를 DB에 저장하기 위해 Client Entity로 변환하는 메소드
     * @param registeredClient
     * @return
     */
    private Client toEntity(RegisteredClient registeredClient) {

        List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods()
                .forEach(clientAuthenticationMethod ->
                clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

           List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
                authorizationGrantTypes.add(authorizationGrantType.getValue()));

          Client entity = new Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

        return entity;
    }

    /**
     * JSON 형식의 문자열을 Map 형식으로 변환하는 메소드
     * @param data
     * @return Map&lt;String, Object&gt;
     */
    private Map<String, Object> parseMap(String data) {
        try {
            return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     * Map 형식을 JSON 형식으로 직렬화하는 메소드
     * @param data
     * @return String
     */
    private String writeMap(Map<String, Object> data) {
        try {

            return this.objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     * 문자열 형태인 AuthorizationGrantType을 AuthorizationGrantType 열거형 형태로 변환하는 메소드
     * @param authorizationGrantType
     * @return
     */
    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;

//        } else if (AuthorizationGrantType.JWT_BEARER.getValue().equals(authorizationGrantType)) {
//            return AuthorizationGrantType.JWT_BEARER;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }

        return new AuthorizationGrantType(authorizationGrantType);
    }

    /**
     * 문자열 형태인 ClientAuthenticationMethod를 ClientAuthenticationMethod 열거형으로 변환하는 메소드
     * @param clientAuthenticationMethod
     * @return
     */
    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }

        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }


}
