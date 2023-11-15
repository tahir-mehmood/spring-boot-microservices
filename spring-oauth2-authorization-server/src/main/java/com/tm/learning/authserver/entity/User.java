package com.tm.learning.authserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 회원 가입 된 사용자 entity
 */
@Entity(name = "app_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {


    @Column(name = "username", nullable = false)
    private String username;


    @Column(name = "password", nullable = false)
    private String password;


    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;


    @Column(name = "active")
    private Boolean active;

}
