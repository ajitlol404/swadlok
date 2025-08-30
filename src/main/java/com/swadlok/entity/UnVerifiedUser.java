package com.swadlok.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Entity
@Table(name = "unverified_users")
public class UnVerifiedUser extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

    @Column(nullable = false, length = 10, unique = true)
    private String phoneNumber;

}
