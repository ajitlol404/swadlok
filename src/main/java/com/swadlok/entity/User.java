package com.swadlok.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    private String image;

    @PrePersist
    private void generateUserId() {
        if (this.code == null) {
            String prefix = switch (this.role) {
                case ROLE_ADMIN -> "ADM";
                case ROLE_CUSTOMER -> "CUS";
                case ROLE_MANAGER -> "MAN";
                case ROLE_DELIVERY -> "DEL";
            };
            this.code = generateReadableId(prefix);
        }
    }

    public enum Role {

        ROLE_ADMIN,
        ROLE_CUSTOMER,
        ROLE_MANAGER,
        ROLE_DELIVERY

    }

}
