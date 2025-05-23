package com.cloud.proxyserver.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // ✅ 예약어 피해가기
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}