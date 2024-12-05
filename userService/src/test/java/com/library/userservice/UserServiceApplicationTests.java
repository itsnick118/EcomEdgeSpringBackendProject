package com.library.userservice;

import com.library.userservice.Security.Services.JpaOAuth2AuthorizationService;
import com.library.userservice.Security.Services.JpaRegisteredClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.UUID;

@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	private JpaRegisteredClientRepository jpaRegisteredClientRepository;
    @Autowired
    private JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService;

	@Test
	void contextLoads() {
	}
//To add client data in the database we have used this trick
	/*@Test
	public void addSampleRegisteredClient(){
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("$2a$12$jFwVAhTw7Fcvw/BsPLtCAOBjEQjm8wRrxWCJayKgUzqG76MURW5Tu")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("ADMIN")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
		jpaRegisteredClientRepository.save(oidcClient);

	}*/
}
