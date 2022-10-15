CREATE TABLE LECTURES
(
    ID            LONG PRIMARY KEY NOT NULL,
    NAME          VARCHAR(255)     NOT NULL,
    THEMATIC_PATH VARCHAR(255)     NOT NULL,
    START_TIME    TIME             NOT NULL
);

CREATE TABLE USERS
(
    ID                 LONG PRIMARY KEY NOT NULL,
    LOGIN              VARCHAR(255),
    EMAIL              VARCHAR(255),
    ENCRYPTED_PASSWORD VARCHAR(255)
);

CREATE TABLE ROLES
(
    ID   LONG PRIMARY KEY NOT NULL,
    NAME VARCHAR(255)     NOT NULL
);

CREATE TABLE USER_LECTURE
(
    ID         LONG PRIMARY KEY NOT NULL,
    USER_ID    LONG             NOT NULL,
    LECTURE_ID LONG             NOT NULL
);

CREATE TABLE USERS_ROLES
(
    ID      LONG PRIMARY KEY NOT NULL,
    USER_ID LONG             NOT NULL,
    ROLE_ID LONG             NOT NULL
);

INSERT INTO LECTURES(ID, NAME, THEMATIC_PATH, START_TIME)
VALUES (1, 'Lecture 1 at 10:00', 'Frontend', '10:00:00'),
       (2, 'Lecture 2 at 10:00', 'Backend', '10:00:00'),
       (3, 'Lecture 3 at 10:00', 'Architect', '10:00:00'),
       (4, 'Lecture 1 at 12:00', 'Frontend', '12:00:00'),
       (5, 'Lecture 2 at 12:00', 'Backend', '12:00:00'),
       (6, 'Lecture 3 at 12:00', 'Architect', '12:00:00'),
       (7, 'Lecture 1 at 14:00', 'Frontend', '14:00:00'),
       (8, 'Lecture 2 at 14:00', 'Backend', '14:00:00'),
       (9, 'Lecture 3 at 14:00', 'Architect', '14:00:00');

INSERT INTO ROLES(ID, NAME)
VALUES (1, 'ROLE_ORGANIZER'),
       (2, 'ROLE_USER');

INSERT INTO USERS(ID, LOGIN, EMAIL, ENCRYPTED_PASSWORD)
VALUES (1, 'test', 'test@test.com', 'mbpHtTXoOpvpAO/3tp9eEg=='), -- test
       (2, 'organizator1', 'organizator@organizator.com', 'Q5LYX2IlO8R0vxaNaOQjvhvwD74epinq'), -- organizator1
       (3, 'username1', 'username1@username1.com', '0ub+5VN40O4qQBT9ZhLi+AcPlLci3UjC'), -- rpP3rU0tqT9nGyr
       (4, 'username2', 'username2@username2.com', 'QefDCX+8zYaMJ09bJi+3SdJoD64aQnpe'), -- zHY70ThUpfLERKq
       (5, 'username3', 'username3@username3.com', 'ycLhHFOgNJxW+J7PDVq9wrRTHRZ12E6a'), -- kajemsTQ9U4YhRu
       (6, 'username4', 'username4@username4.com', 'vVVg6HGtdtxWgTrRju6Ru9vrfl5+FFHF'), -- pOmqK1Ad0F3Fn0E
       (7, 'username5', 'username5@username5.com', 'qhWm/7Cw4Fnji0bOw9m164JHorvgQ9IF'), -- qtBc4TPpkDxrAYP
       (8, 'username6', 'username6@username6.com', 'axpMbnqBO/Tt8Y1YUgbg+dTKQgXIv4jR'), -- Lb70q7X2dzglZk2
       (9, 'username7', 'username7@username7.com', 'tU4G74z/a1ISN7DiY9LWCHJzzgelHQFf'), -- 1dz2QeQlDGMYg32
       (10, 'username8', 'username8@username8.com', 'pxBA+hFjds0DWsZFgxSyc5i2DeHZVyEw'); -- TRZ3w78j6pAcyf8

INSERT INTO USERS_ROLES(ID, USER_ID, ROLE_ID)
VALUES (1, 1, 2),
       (2, 2, 1),
       (3, 3, 2),
       (4, 4, 2),
       (5, 5, 2),
       (6, 6, 2),
       (7, 7, 2),
       (8, 8, 2),
       (9, 9, 2),
       (10, 10, 2);

INSERT INTO USER_LECTURE(ID, USER_ID, LECTURE_ID)
VALUES
    ( 1, 3, 1 ),
    ( 2, 4, 1 ),
    ( 3, 5, 1 ),
    ( 4, 6, 1 ),
    ( 5, 7, 1 ),
    ( 6, 3, 5 ),
    ( 7, 4, 5 ),
    ( 8, 5, 5 ),
    ( 9, 6, 5 ),
    ( 10, 8, 8 ),
    ( 11, 9, 8 ),
    ( 12, 10, 8 );
