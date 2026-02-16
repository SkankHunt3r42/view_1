package com.example.test_tasks.controllers.services;

import com.example.test_tasks.entites.enums.AllowedProviders;
import com.example.test_tasks.entites.UserEntity;
import com.example.test_tasks.entites.dtos.requests.LogInRequest;
import com.example.test_tasks.entites.dtos.requests.SignInRequest;
import com.example.test_tasks.handling.exceptions.NotReachAbleEmailException;
import com.example.test_tasks.handling.exceptions.ObjectAlreadyExistsException;
import com.example.test_tasks.handling.exceptions.ObjectNotFoundException;
import com.example.test_tasks.handling.exceptions.WrongCredentialException;
import com.example.test_tasks.repo.UserRepo;
import com.example.test_tasks.security.services.JWTManagerService;
import com.example.test_tasks.services.MailManagerService;
import com.example.test_tasks.utils.GlobalUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntryService {

    UserRepo userRepo;
    MailManagerService mailManager;
    RedisTemplate<String,Object> redisTemplate;
    PasswordEncoder encoder;
    JWTManagerService jwtManager;

    public ResponseEntity<Void> sign(SignInRequest dto) {

        if(userRepo.existsByEmail(dto.getEmail())){
            throw new ObjectAlreadyExistsException("User with this email is already exists");
        }

        if(!mailManager.hasMxRecord(dto.getEmail())){
            throw new NotReachAbleEmailException("Server cant reach the email");
        }
        UserEntity user = UserEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .surname(dto.getSurname())
                .password(encoder.encode(dto.getPassword()))
                .build();

        userRepo.save(user);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> login(LogInRequest dto) {

        UserEntity pendingUser = userRepo.findByEmail(dto.getEmail())
                .filter(e -> encoder.matches(dto.getPassword(), e.getPassword()))
                .orElseThrow(() -> new WrongCredentialException("Provided wrong credentials"));

        HttpHeaders headers = jwtManager.
                getTokensHeadersWrapped(jwtManager.generateTokenPairs(pendingUser.getEmail(),AllowedProviders.LOCAL));

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    public ResponseEntity<Void> pending_verify(String email) {

        if(!userRepo.existsByEmail(email)){
            throw new ObjectNotFoundException("Provided email are not linked to any of users");
        }

        String code = GlobalUtils.randomCodeGenerator();

        mailManager.sendVerificationEmail(email,code);

        redisTemplate.opsForValue().set("pendingV:"+code,email, Duration.ofMinutes(5));

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> verify (String code) {

        String email = (String) redisTemplate.opsForValue()
                .getAndDelete("pendingV:"+code);

        if(email != null){
            userRepo.updateUserVerifiedStatus(email);

            return ResponseEntity.ok().build();
        }

        throw new ObjectNotFoundException("Code expired");
    }

}
