package com.pp.userservice;

import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.LectureRepository;
import com.pp.userservice.lecture.dto.LectureSignUpDTO;
import com.pp.userservice.role.RoleService;
import com.pp.userservice.user.api.UserDTO;
import com.pp.userservice.user.entity.UserEntity;
import com.pp.userservice.user.service.UserService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Deprecated(since = "Intern-task 2.1.0")
class SetupDataLoader implements CommandLineRunner {

  boolean alreadySetup = true;

  private final UserService userService;
  private final LectureRepository lectureRepository;
  private final RoleService roleService;

  @Override
  public void run(String... args) throws Exception {
    loadInitialData();
  }

  public void loadInitialData() {

    if (alreadySetup)
      return;

    roleService.createRoleIfNotFound("ROLE_ORGANIZER");
    roleService.createRoleIfNotFound("ROLE_USER");

    String thematicPathFrontend = "Frontend";
    String thematicPathBackend = "Backend";
    String thematicPathArchitect = "Architect";

    /*
     * Initial data, list of users and list of lectures
     */
    List<LectureEntity> lectureEntityList = new ArrayList<>(Arrays.asList( //
        new LectureEntity("Lecture 1 at 10:00", thematicPathFrontend, LocalTime.of(10, 0)), //
        new LectureEntity("Lecture 2 at 10:00", thematicPathBackend, LocalTime.of(10, 0)), //
        new LectureEntity("Lecture 3 at 10:00", thematicPathArchitect, LocalTime.of(10, 0)), //
        new LectureEntity("Lecture 1 at 12:00", thematicPathFrontend, LocalTime.of(12, 0)), //
        new LectureEntity("Lecture 2 at 12:00", thematicPathBackend, LocalTime.of(12, 0)), //
        new LectureEntity("Lecture 3 at 12:00", thematicPathArchitect, LocalTime.of(12, 0)), //
        new LectureEntity("Lecture 1 at 14:00", thematicPathFrontend, LocalTime.of(14, 0)), //
        new LectureEntity("Lecture 2 at 14:00", thematicPathBackend, LocalTime.of(14, 0)), //
        new LectureEntity("Lecture 3 at 14:00", thematicPathArchitect, LocalTime.of(14, 0))));
    lectureRepository.saveAll(lectureEntityList);

    /**
     * Adding user sign in to lecture Lecutre 1 at 10:00 - 5/5 Lecture 2 at 12:00 - 4/5 Lecture 2 at
     * 14:00 - 3/5
     */
    List<LectureSignUpDTO> userList = new ArrayList<>(Arrays.asList(

        new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username1", "mail1@mail.com")), //
        new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username2", "mail2@mail.com")), //
        new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username3", "mail3@mail.com")), //
        new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username4", "mail4@mail.com")), //
        new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username5", "mail5@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username1", "mail1@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username2", "mail2@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username3", "mail3@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username4", "mail4@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username6", "mail6@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username7", "mail7@mail.com")), //
        new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username8", "mail8@mail.com"))));
    userList.forEach(user -> userService.findUserByLogin(user.getUserDTO()
        .getLogin())
        .ifPresentOrElse(userEntity -> userService.signUpForLecture(userEntity, user.getLectureName()), () -> userService.signUp(user)));

    /**
     * Saving one user with organizer role and other normal username for test purspose (bypassing the
     * password generator)
     */

    UserEntity userOrganizator = userService.createUserWithPassword(new UserDTO("organizator1", "organizator1@gmail.com"), "organizator1");
    userOrganizator.getRoles()
        .add(roleService.findByName("ROLE_ORGANIZER"));
    userService.saveUser(userOrganizator);

    UserEntity userTest = userService.createUserWithPassword(new UserDTO("test", "test@gmail.com"), "test");
    userTest.getRoles()
        .add(roleService.findByName("ROLE_USER"));
    userService.saveUser(userTest);

    alreadySetup = true;
  }
}
