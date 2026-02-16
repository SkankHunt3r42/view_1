package com.example.test_tasks.security;


import com.example.test_tasks.security.filters.JWTAuthFilter;
import com.example.test_tasks.security.handlers.FailureHandler;
import com.example.test_tasks.security.handlers.SuccessHandler;
import com.example.test_tasks.security.services.O2AuthManagerService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterChain {

    JWTAuthFilter filter;
    O2AuthManagerService o2a;
    ClientRegistrationRepository repository;
    SuccessHandler successHandler;
    FailureHandler failureHandler;

    @Bean
    public SecurityFilterChain chain(HttpSecurity http){

        http.authorizeHttpRequests(e -> {

            e.requestMatchers("/entry/***").permitAll();
            e.anyRequest().authenticated();

        });

        http.exceptionHandling(c -> c
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));


        http.oauth2Login(e -> {

            e.userInfoEndpoint(var -> var.userService(o2a));

            e.authorizationEndpoint(var ->
                    var.authorizationRequestResolver(resolver(repository)));

            e.successHandler(successHandler);

            e.failureHandler(failureHandler);

        });

        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(filter, AnonymousAuthenticationFilter.class);
        http.sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public DefaultOAuth2AuthorizationRequestResolver resolver(ClientRegistrationRepository repository){

        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(repository,"/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(var -> {
            var.additionalParameters(var1 -> var1.put("promt","login"));
        });

        return resolver;
    }

//    @Bean
//    public CorsConfigurationSource cors() {
//
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of("SOMEORIGIN"));
//
//        configuration.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTIONS"));
//
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
//
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//
//    }
}
