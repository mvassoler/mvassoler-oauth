package com.mvassoler.oauht.mvassoleroauth.domain;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "account")
public class User extends BaseEntityModel implements Serializable {

    private static final long serialVersionUID = 4142353297854718047L;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "reset_password_required", nullable = false)
    private boolean resetPasswordRequired;

    @Column(name = "username", length = 40, nullable = false)
    private String username;

    @Column(name = "super", nullable = false)
    private boolean superUser;

    @Column(name = "first_name", length = 80, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 80, nullable = false)
    private String lastName;

    @Column(name = "customer", nullable = false)
    private boolean customer;

}
