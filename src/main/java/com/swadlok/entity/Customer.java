package com.swadlok.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static org.hibernate.type.SqlTypes.JSON;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @JdbcTypeCode(JSON)
    private UserData userData;

    @JdbcTypeCode(JSON)
    private List<Address> addresses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Embeddable
    public static class UserData implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private UUID secretKey;
        private boolean secretKeyStatus = false;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class Address implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private Integer postalCode;
        private boolean isDefault;
        private String landmark;
        private String deliveryInstructions;
    }
}