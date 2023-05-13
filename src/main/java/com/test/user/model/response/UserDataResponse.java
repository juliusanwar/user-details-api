package com.test.user.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDataResponse implements Serializable {
    private Long id;
    private String ssn;
    private String first_name;
    private String last_name;
    private String birth_date;
    private String created_time;
    private String updated_time;
    private String created_by;
    private String updated_by;
    private boolean is_active;
}
