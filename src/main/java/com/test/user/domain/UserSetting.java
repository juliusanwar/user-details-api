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

@Entity
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "user_setting")
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_setting_id", unique = true)
    private Long id;
    @Column(name = "user_setting_key", nullable = false)
    @Size(min = 3, max = 100)
    private String key;
    @Column(name = "user_setting_value", nullable = false)
    @Size(min = 3, max = 100)
    private String value;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private Users user_id;
}