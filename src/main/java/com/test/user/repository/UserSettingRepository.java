package com.test.user.repository;

import com.test.user.domain.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    @Query("select us from UserSetting us WHERE us.user_id.id = ?1 ")
    List<UserSetting> find_by_id_user(Long id);
}
