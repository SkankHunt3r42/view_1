package com.example.test_tasks.utils.security;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@UtilityClass
public final class SecurityUtils {


    public Collection<? extends GrantedAuthority> convertRoleIntoAuthority(String roleName){
        return Collections.singleton(new SimpleGrantedAuthority(roleName));
    }

    public String createRandomPassword(){

        return UUID.randomUUID().toString().substring(0,8);
    }

    public String defineUserNameAttributeName(OAuth2UserRequest userRequest){

        return userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    }

    public String getProviderName(OAuth2UserRequest userRequest){


        return userRequest.getClientRegistration().getRegistrationId().toUpperCase();
    }


}
