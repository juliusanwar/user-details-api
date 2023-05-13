package com.test.user;

import com.test.user.domain.Users;
import com.test.user.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTests {
    @Autowired
    private UsersRepository users_repository;

    // First we need to create the user first before call the other repository to find the specific user / all users

    @Test
    @Order(1)
    @Rollback(value = false)
    void create_user_test(){
        Date date = new Date();
        LocalDateTime datetime = LocalDateTime.now();
        Users user = Users.builder()
                .ssn("0000000000002945")
                .first_name("Jon")
                .family_name("Snow")
                .birth_date(date)
                .created_time(datetime)
                .updated_time(datetime)
                .created_by("SYSTEM")
                .updated_by("SYSTEM")
                .is_active(true)
                .build();

        users_repository.save(user);

        Assertions.assertThat(user.getId()).isPositive();
    }

    @Test
    @Order(2)
    void get_user_by_id_test(){

        Users user = users_repository.findById(1L).get();

        Assertions.assertThat(user.getId()).isEqualTo(1L);

    }

    @Test
    @Order(3)
    void get_all_users(){

        List<Users> user = users_repository.findAll();

        Assertions.assertThat(user.size()).isPositive();

    }
}
