package com.pp.userservice.lecture;

import com.pp.userservice.lab3.InterfacesSegragation.Classes.BasicUserAuth;
import com.pp.userservice.lab3.InterfacesSegragation.Classes.EmailNotifier;
import com.pp.userservice.lab3.InterfacesSegragation.Classes.LectureManager;
import com.pp.userservice.lab3.InterfacesSegragation.Classes.UserLectureSubscription;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureAssignment;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureCreation;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Notification.EmailNotificationInterface;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User.UserAuthentication;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User.UserSubscription;
import com.pp.userservice.lab3.Lecture.InPersonLecture;
import com.pp.userservice.lab3.Lecture.Lecture;
import com.pp.userservice.lab3.Lecture.OnlineLecture;
import com.pp.userservice.lab3.Notification.EmailNotification;
import com.pp.userservice.lab3.Notification.NotificationSender;
import com.pp.userservice.lab3.Payment.CardPayment;
import com.pp.userservice.lab3.Payment.PaymentProcessor;
import com.pp.userservice.lab3.Reservation.LectureReservation;
import com.pp.userservice.lab3.Reservation.LectureReservationManager;
import com.pp.userservice.lab3.Reservation.ReservationSystem;
import com.pp.userservice.lab3.Syllabus.AdvancedSyllabus;
import com.pp.userservice.lab3.Syllabus.BasicSyllabus;
import com.pp.userservice.lab3.Syllabus.Syllabus;
import com.pp.userservice.lab3.User.AdminUser;
import com.pp.userservice.lab3.User.RegularUser;
import com.pp.userservice.lab3.User.User;
import com.pp.userservice.lecture.DesignPatterns.Command.DeleteLectureCommand;
import com.pp.userservice.lecture.DesignPatterns.Command.LectureDeletingInvoker;
import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.NotificationEntity;
import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.UserNotification;
import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Observer.Notification;
import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Service.NotificationsProvider;
import com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure.NotificationRepository;
import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureDetailsDTO;
import com.pp.userservice.lecture.dto.LectureIteratedDTO;
import com.pp.userservice.lecture.service.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pp.userservice.lecture.LectureController.LECTURES_ENDPOINT;

@RestController
@RequestMapping(value = LECTURES_ENDPOINT)
@AllArgsConstructor
class LectureController {

    static final String LECTURES_ENDPOINT = "/lectures";
    private final LectureService lectureService;
    private final NotificationRepository notificationRepository;
    private NotificationsProvider notificationsProvider;

    /**
     * End point for method GET http://localhost:8080/lectures
     *
     * @return list of all lectures stored in database
     */
    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureDTO> getLectures() {
        return lectureService.getLectures();
    }

    @GetMapping(path = "/details")
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureDetailsDTO> getLecturesDetails() {
        return lectureService.getLecturesDetails();
    }

    //l2z1
    @DeleteMapping(path = "/delete/{thematicPath}")
    @ResponseStatus(value = HttpStatus.OK)
    void deleteLecture(@PathVariable String thematicPath) {
        DeleteLectureCommand command = new DeleteLectureCommand(lectureService, thematicPath);
        LectureDeletingInvoker invoker = new LectureDeletingInvoker();

        invoker.setCommand(command);
        invoker.execute();
    }

    //l2z2
    @GetMapping("/expressions/{thematicPath1}/{thematicPath2}/{startTime}")
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureDTO> getLecturesByExpressions(
            @PathVariable String thematicPath1,
            @PathVariable String thematicPath2,
            @PathVariable String startTime)
    {
        return lectureService.getLecturesByExpressions(thematicPath1, thematicPath2, startTime);
    }

    //l3z3
    @GetMapping(path = "/iterated")
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureIteratedDTO> getLecturesIterated() {
        return lectureService.getLecturesIterated();
    }

    //l2z8
    @GetMapping(path = "/syllabus/{thematicPath}")
    @ResponseStatus(value = HttpStatus.OK)
    List<Map<String, List<String>>> getSyllabus(@PathVariable String thematicPath) {
        return lectureService.getLecureSyllabus(thematicPath);
    }

    //l2z6
    @GetMapping(path = "/notifications")
    @ResponseStatus(value = HttpStatus.OK)
    List<NotificationEntity> getNotificationsAction() {
        return notificationsProvider.getNotifications();
    }

    //l2z6
    @PostMapping(path = "/notify")
    @ResponseStatus(value = HttpStatus.OK)
    void notify(@RequestParam String message) {
        List<UserNotification> observers = new ArrayList<>();

        UserNotification user = new UserNotification(3);

        observers.add(new UserNotification(2));
        observers.add(user);
        observers.add(new UserNotification(5));

        observers.remove(user);

        Notification notification = new Notification(notificationRepository, observers, message);

        notification.notifyObservers();
    }

    //l3z1
    //l3z9
    @GetMapping(path = "/display/syllabus")
    @ResponseStatus(value = HttpStatus.OK)
    List<String> displaySyllabus() {
        List<String> result = new ArrayList<>();

        Lecture onlineLecture = new OnlineLecture("Java Basics");
        Lecture inPersonLecture = new InPersonLecture("Advanced Java");

        Syllabus basicSyllabus = new BasicSyllabus();
        Syllabus advancedSyllabus = new AdvancedSyllabus();

        onlineLecture.setSyllabus(basicSyllabus);
        inPersonLecture.setSyllabus(advancedSyllabus);

        try {
            result.add(onlineLecture.showSyllabus());
            result.add(inPersonLecture.showSyllabus());
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Syllabus not available for this lecture", e);
        }

        return result;
    }

    //l3z1
    //l3z9
    @GetMapping(path = "/display/users")
    @ResponseStatus(value = HttpStatus.OK)
    List<String> displayUsers() {
        List<String> result = new ArrayList<>();

        User regularUser = new RegularUser("user");
        User adminUser = new AdminUser("admin");

        Lecture onlineLecture = new OnlineLecture("Java Basics");
        Lecture inPersonLecture = new InPersonLecture("Advanced Java");

        try {
            result.add(regularUser.attendLecture(onlineLecture));
            result.add(adminUser.attendLecture(inPersonLecture));

            Lecture anotherOnlineLecture = new OnlineLecture("Data Structures");

            result.add(regularUser.attendLecture(anotherOnlineLecture));
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecture is full or unavailable", e);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not allowed for this lecture", e);
        }

        return result;
    }

    //l3z2
    //l3z9
    @GetMapping(path = "/display/dependencyInversion")
    @ResponseStatus(value = HttpStatus.OK)
    List<String> displayDependencyInversion() {
        ReservationSystem reservationSystem = new LectureReservation("R123");
        PaymentProcessor paymentProcessor = new CardPayment("P456");
        NotificationSender notificationSender = new EmailNotification("user@example.com");

        LectureReservationManager manager = new LectureReservationManager(reservationSystem, paymentProcessor, notificationSender);

        try {
            return manager.reserveAndNotify("user", "Java Basics", 50.0);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation parameters", e);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment processing failed or reservation conflict", e);
        }
    }

    //l3z3
    @GetMapping(path = "/display/interfaceSegragation")
    @ResponseStatus(value = HttpStatus.OK)
    List<String> displayInterfaceSegragation() {
        List<String> result = new ArrayList<>();

        UserAuthentication auth = new BasicUserAuth();
        result.add(auth.register("user"));
        result.add(auth.login("user"));

        UserSubscription subscription = new UserLectureSubscription();
        result.add(subscription.subscribeToLecture("Java Basics"));

        LectureCreation lectureManager = new LectureManager();
        result.add(lectureManager.createLecture("Advanced Java"));

        LectureAssignment assignment = new LectureManager();
        result.add(assignment.assignTeacher("Advanced Java", "Dr. Smith"));

        EmailNotificationInterface emailNotifier = new EmailNotifier();
        result.add(emailNotifier.sendEmail("user@example.com", "You have a new message!"));

        return result;
    }
}
