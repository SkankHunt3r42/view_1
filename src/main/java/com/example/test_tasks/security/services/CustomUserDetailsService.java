package com.example.test_tasks.security.services;

import com.example.test_tasks.entites.UserEntity;
import com.example.test_tasks.handling.exceptions.ObjectNotFoundException;
import com.example.test_tasks.repo.UserRepo;
import com.example.test_tasks.utils.security.SecurityUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    UserRepo repo;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = repo.findByEmail(username)
                .orElseThrow(() -> new ObjectNotFoundException("Provided user not exists"));

        return new User(user.getName(),
                user.getPassword(),
                SecurityUtils.convertRoleIntoAuthority(user.getRole().name()));
    }
}
