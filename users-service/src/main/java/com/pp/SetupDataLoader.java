package com.pp;

import com.pp.lecture.LectureEntity;
import com.pp.lecture.LectureRepository;
import com.pp.lecture.dto.LectureSignUpDTO;
import com.pp.lecture.service.LectureService;
import com.pp.role.RoleService;
import com.pp.user.UserDTO;
import com.pp.user.UserEntity;
import com.pp.user.UserRepository;
import com.pp.user.service.UserService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  boolean alreadySetup = false;

  private final UserService userService;
  private final LectureService lectureService;
  private final UserRepository userRepository;
  private final LectureRepository lectureRepository;
  private final RoleService roleService;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

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
    userList.forEach(user -> userRepository.findByLogin(user.getUserDTO()
        .getLogin())
        .ifPresentOrElse(userEntity -> userService.signUpForLecture(userEntity, user.getLectureName()), () -> userService.signUp(user)));

    /**
     * Saving one user with organizer role and other normal username for test purspose (bypassing the
     * password generator)
     */

    UserEntity userOrganizator = userService.createUserWithPassword(new UserDTO("organizator1", "organizator1@gmail.com"), "organizator1");
    userOrganizator.getRoles()
        .add(roleService.findByName("ROLE_ORGANIZER"));
    userRepository.save(userOrganizator);

    UserEntity userTest = userService.createUserWithPassword(new UserDTO("test", "test@gmail.com"), "test");
    userTest.getRoles()
        .add(roleService.findByName("ROLE_USER"));
    userRepository.save(userTest);

    alreadySetup = true;
  }
}
