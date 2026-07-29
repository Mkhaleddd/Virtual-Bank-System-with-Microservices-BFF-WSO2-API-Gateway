package com.vbank.user_service.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "user_schema")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    String username;

    @Column(nullable = false, length = 100)
    String firstName;

    @Column(nullable = false, length = 100)
    String lastName;

    @Column(unique = true, nullable = false, length = 150)
    String email;

    @Column(nullable = false)
    String passwordHash;
}
