package com.test.user.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "users") // change to users since user already reserved in the database engine
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true)
    private Long id;

    @Column(name = "ssn", length = 16, nullable = false, unique = true)
    @Size(min = 16, max = 16)
    private String ssn;
    @Column(name = "first_name", nullable = false)
    @Size(min = 3, max = 100)
    private String first_name;
    @Column(name = "middle_name")
    @Size(min = 3, max = 100)
    private String middle_name;
    @Column(name = "family_name", nullable = false)
    @Size(min = 3, max = 100)
    private String family_name;
    @Column(name = "birth_date")
    private Date birth_date;
    @Column(name = "created_time", nullable = false)
    private LocalDateTime created_time;
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updated_time;
    @Column(name = "created_by", nullable = false)
    private String created_by = "SYSTEM";
    @Column(name = "updated_by", nullable = false)
    private String updated_by = "SYSTEM";
    @Column(name = "is_active", nullable = false)
    private Boolean is_active;
    @Column(name = "deleted_time")
    private LocalDateTime deleted_time;

    @OneToMany()
    private List<UserSetting> userSetting;

}
