package com.test.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.user.domain.UserSetting;
import com.test.user.domain.Users;
import com.test.user.model.request.UserRequest;
import com.test.user.model.response.UserDataResponse;
import com.test.user.model.response.UserResponse;
import com.test.user.model.response.UsersResponse;
import com.test.user.repository.UsersRepository;
import com.test.user.repository.UserSettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UserSettingRepository userSettingRepository;

    public UsersResponse get_all_users(int max_records, int offset) {
        try{
            List<Users> users = usersRepository.find_user_with_limit_and_offset(max_records, offset);
            List<UserDataResponse> user_data_response = new ArrayList<>();
            if(!users.isEmpty()) {
                for (Users user : users) {
                    if(user.getIs_active() != null && user.getIs_active().equals(true)) {
                        user_data_response.add(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .first_name(user.getFirst_name())
                                .last_name(user.getFamily_name())
                                .birth_date(user.getBirth_date().toString())
                                .created_time(user.getCreated_time().toString())
                                .updated_time(user.getUpdated_time().toString())
                                .created_by(user.getCreated_by())
                                .updated_by(user.getUpdated_by())
                                .is_active(user.getIs_active())
                                .build());
                    }
                }
            }
            return UsersResponse.builder()
                    .user_data(user_data_response)
                    .max_records(max_records)
                    .offset(offset)
                    .build();
        } catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserResponse create_user(UserRequest user_request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            LocalDateTime datetime = LocalDateTime.now();

            Users get_ssn_by_id = usersRepository.get_user_by_ssn(user_request.getSsn());
            if (get_ssn_by_id != null && get_ssn_by_id.getId() > 0) {
                return null;
            }
            Users user_new = Users.builder()
                    .ssn(user_request.getSsn())
                    .first_name(user_request.getFirst_name())
                    .family_name(user_request.getLast_name())
                    .birth_date(user_request.getBirth_date())
                    .created_by("SYSTEM")
                    .updated_by("SYSTEM")
                    .created_time(datetime)
                    .updated_time(datetime)
                    .is_active(true)
                    .build();

            Users user = usersRepository.save(user_new);

            Map<String, String> userSet = new HashMap<>();

            String[] settings= {"biometric_login","push_notification","sms_notification","show_onboarding","widget_order"};
            for (int i = 0; i < settings.length; i++) {
                UserSetting uSetting = new UserSetting();
                uSetting.setUser_id(user);
                uSetting.setKey(settings[i]);
                if(i == 4){
                    uSetting.setValue("1,2,3,4,5");
                    userSet.put(settings[i],"1,2,3,4,5");
                }else {
                    uSetting.setValue("false");
                    userSet.put(settings[i],"false");
                }
                userSettingRepository.save(uSetting);
            }
            UserResponse response = UserResponse.builder()
                    .userData(UserDataResponse.builder()
                            .id(user.getId())
                            .ssn(user.getSsn())
                            .first_name(user.getFirst_name())
                            .last_name(user.getFamily_name())
                            .birth_date(user.getBirth_date().toString())
                            .created_time(user.getCreated_time().toString())
                            .updated_time(user.getUpdated_time().toString())
                            .created_by(user.getCreated_by())
                            .updated_by(user.getUpdated_by())
                            .is_active(user.getIs_active())
                            .build())
                    .build();

            List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());
            response.setUserSettings(listAll);

            return response;
        } catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserResponse get_user_by_id(Long id) {
        try {
            Optional<Users> userDb = usersRepository.findById(id);
            Users user = new Users();
            if(userDb.isPresent()) {
                user = userDb.get();
            }
            if(user.getIs_active() != null && user.getIs_active().equals(true)) {
                List<UserSetting> uSets = userSettingRepository.find_by_id_user(id);

                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());


                return UserResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .first_name(user.getFirst_name())
                                .last_name(user.getFamily_name())
                                .birth_date(user.getBirth_date().toString())
                                .created_time(user.getCreated_time().toString())
                                .updated_time(user.getUpdated_time().toString())
                                .created_by(user.getCreated_by())
                                .updated_by(user.getUpdated_by())
                                .is_active(user.getIs_active())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UserResponse update_user(UserRequest request, Long id) {
        try {
            Optional<Users> userDb = usersRepository.findById(id);
            Users user = new Users();
            if(userDb.isPresent()) {
                user = userDb.get();
            }
            if(user.getIs_active() != null && user.getIs_active().equals(true)) {
                user.setSsn(request.getSsn());
                user.setFirst_name(request.getFirst_name());
                user.setFamily_name(request.getLast_name());
                user.setBirth_date(request.getBirth_date());
                user.setUpdated_time(LocalDateTime.now());

                usersRepository.save(user);

                List<UserSetting> uSets = userSettingRepository.find_by_id_user(id);

                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());

                return UserResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .first_name(user.getFirst_name())
                                .last_name(user.getFamily_name())
                                .birth_date(user.getBirth_date().toString())
                                .created_time(user.getCreated_time().toString())
                                .updated_time(user.getUpdated_time().toString())
                                .created_by(user.getCreated_by())
                                .updated_by(user.getUpdated_by())
                                .is_active(user.getIs_active())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public UserResponse update_user_setting(List<Map<String, String>> userSettings, Long id) {
        try {
            Optional<Users> getOneUser = usersRepository.findById(id);
            Users user = new Users();
            if(getOneUser.isPresent()) {
                user = getOneUser.get();
            }

            if(user.getIs_active() != null && user.getIs_active().equals(true)) {
                user.setUpdated_time(LocalDateTime.now());
                usersRepository.save(user);

                List<UserSetting> uSetRes = userSettingRepository.find_by_id_user(id);

                for (UserSetting update : uSetRes) {
                    UserSetting getOne = userSettingRepository.findById(update.getId()).get();
                    for (Map<String, String> map : userSettings) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (Objects.equals(getOne.getKey(), entry.getKey())) {
                                getOne.setValue(entry.getValue());
                            }
                            userSettingRepository.save(getOne);
                        }
                    }
                }

                List<UserSetting> uSets = userSettingRepository.find_by_id_user(id);
                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());

                return UserResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .first_name(user.getFirst_name())
                                .last_name(user.getFamily_name())
                                .birth_date(user.getBirth_date().toString())
                                .created_time(user.getCreated_time().toString())
                                .updated_time(user.getUpdated_time().toString())
                                .created_by(user.getCreated_by())
                                .updated_by(user.getUpdated_by())
                                .is_active(user.getIs_active())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Users delete_user(Long id) {
        try {
            Optional<Users> userDb = usersRepository.findById(id);
            Users user = new Users();
            if(userDb.isPresent()) {
                user = userDb.get();
            }
            if(user.getIs_active() != null && user.getIs_active().equals(true)) {
                user.setIs_active(false);
                user.setDeleted_time(LocalDateTime.now());

                usersRepository.save(user);

                return user;
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public UserResponse refresh_user(Long id) {
        try {
            Optional<Users> userDb = usersRepository.findById(id);
            Users user = new Users();
            if(userDb.isPresent()) {
                user = userDb.get();
            }
            if(user.getIs_active() != null && user.getIs_active().equals(false)) {
                user.setIs_active(true);
                user.setUpdated_time(LocalDateTime.now());
                user.setDeleted_time(null);
                usersRepository.save(user);

                List<UserSetting> uSets = userSettingRepository.find_by_id_user(id);

                Map<String, String> userSet = new HashMap<>();

                for (UserSetting uSet : uSets) {
                    userSet.put(uSet.getKey(), uSet.getValue());
                }

                List<Map.Entry<String, String>> listAll = new ArrayList<>(userSet.entrySet());


                return UserResponse.builder()
                        .userData(UserDataResponse.builder()
                                .id(user.getId())
                                .ssn(user.getSsn())
                                .first_name(user.getFirst_name())
                                .last_name(user.getFamily_name())
                                .birth_date(user.getBirth_date().toString())
                                .created_time(user.getCreated_time().toString())
                                .updated_time(user.getUpdated_time().toString())
                                .created_by(user.getCreated_by())
                                .updated_by(user.getUpdated_by())
                                .is_active(user.getIs_active())
                                .build())
                        .userSettings(listAll)
                        .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
