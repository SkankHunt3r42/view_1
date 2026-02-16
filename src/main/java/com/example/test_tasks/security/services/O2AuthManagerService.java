package com.example.test_tasks.security.services;

import com.example.test_tasks.entites.enums.AllowedProviders;
import com.example.test_tasks.entites.UserEntity;
import com.example.test_tasks.repo.UserRepo;
import com.example.test_tasks.utils.security.SecurityUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class O2AuthManagerService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User pendingUser = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = pendingUser.getAttribute("email");

        UserEntity user = userRepo.findByEmail(email).
                orElseGet(() -> createUserFromProviderSources(pendingUser,
                        SecurityUtils.getProviderName(userRequest)));

        String userNameAttributeName = SecurityUtils.defineUserNameAttributeName(userRequest);

        Map<String,Object> modAttr = new HashMap<>(pendingUser.getAttributes());

        modAttr.put("provider",SecurityUtils.getProviderName(userRequest));

        return new DefaultOAuth2User(
                SecurityUtils.convertRoleIntoAuthority(user.getRole().name()),
                modAttr,userNameAttributeName);
    }

    protected UserEntity createUserFromProviderSources(OAuth2User pendingUser,String provider) {

        String password = SecurityUtils.createRandomPassword();

        UserEntity user = UserEntity.builder()
                .name("John")
                .surname("Doe")
                .email(pendingUser.getAttribute("email"))
                .password(password)
                .provider(AllowedProviders.valueOf(provider))
                .is_verified(true)
                .build();

        return userRepo.save(user);
    }

}
