package com.test.user.repository;

import com.test.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u ORDER BY u.id LIMIT ?1 OFFSET ?2")
    List<Users> find_user_with_limit_and_offset(int limit, int offset);
    @Query("SELECT u FROM Users u WHERE u.ssn = ?1")
    Users get_user_by_ssn(String ssn);
}
