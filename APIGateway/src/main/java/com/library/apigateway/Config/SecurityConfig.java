package com.library.apigateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.authorizeExchange(exchanges-> exchanges
                        .pathMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/category/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/products/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers(HttpMethod.PUT,"/products/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE,"/products/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers(HttpMethod.POST,"/category/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers(HttpMethod.PUT,"/category/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE,"/category/**").hasAuthority("SCOPE_ADMIN")
                        .pathMatchers( "/orders/**").hasAuthority("SCOPE_USER")
                        .pathMatchers("/payments/**").permitAll()
                        .pathMatchers("/paymentCallBack/**").permitAll()
                        .pathMatchers("/users/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(Customizer.withDefaults()));
        serverHttpSecurity.csrf(csrfSpec-> csrfSpec.disable());
        return serverHttpSecurity.build();
    }
}

