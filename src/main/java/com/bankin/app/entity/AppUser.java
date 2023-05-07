package com.bankin.app.entity;

import com.bankin.app.dto.auth.InitUserReq;
import com.bankin.app.enums.UserRole;
import com.bankin.app.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.bankin.app.enums.UserStatus.EMAIL_PHONE;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "app_user", schema = "dbo")
public class AppUser extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @ToString.Exclude
    private String password;

    @Column( unique = true)
    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String CCCD;

    @Column(name="dob")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user")
    private List<Face> faces;

    @OneToOne(mappedBy = "user")
    private Account account;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public AppUser(InitUserReq registerReq){
        this.email = registerReq.getEmail();
        this.phoneNumber = registerReq.getPhoneNumber();
        this.role = UserRole.CUSTOMER;
        this.status = EMAIL_PHONE;
    }
}
