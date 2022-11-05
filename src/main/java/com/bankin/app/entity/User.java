package com.bankin.app.entity;

import com.bankin.app.dto.auth.InitUserReq;
import com.bankin.app.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "[User]", schema = "dbo")
public class User extends Auditable<String>{
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

    public User(InitUserReq registerReq){
        this.email = registerReq.getEmail();
        this.phoneNumber = registerReq.getPhoneNumber();
    }
}
