package com.example.test_tasks.entites;

import com.example.test_tasks.entites.enums.AllowedProviders;
import com.example.test_tasks.entites.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;

    String surname;

    String email;

    String password;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    Roles role = Roles.USER;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    AllowedProviders provider = AllowedProviders.LOCAL;

    @Column(nullable = false)
    @Builder.Default
    Boolean is_verified = false;

    @OneToMany(
            mappedBy = "users",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    Set<BookEntity> books;

}
