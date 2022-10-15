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
VALUES (1, 'test', 'test@test.com', '$2a$10$XrGZ4MqlAJzbhEQFUkYKf.UpNw6gPtV0U.bovIVlp8s7Tx4vP.vDi'), -- test
       (2, 'organizator1', 'organizator@organizator.com', '$2a$10$LHWcf3gL3E3Acu3DZy7s/uoSy9iFgOv0Q1JlnJ4OmLfVbucHF5Mkq'), -- organizator1
       (3, 'username1', 'username1@username1.com', '$2a$10$Zeunh4ox/scWB7.pr06S8OhLEB2WuZGzZiqK3PSgXQOmXp.kiJQXW'), -- rpP3rU0tqT9nGyr
       (4, 'username2', 'username2@username2.com', '$2a$10$/5zsyzvZQN.U.Hl3Xcy5rujjJaGsdNC2aeTKoJlX1LozsboE5HrI2'), -- zHY70ThUpfLERKq
       (5, 'username3', 'username3@username3.com', '$2a$10$ql0ni6WYyASsIYuTv6jIl.SXDCHCb89IyiWaCo8EbYSOp9LYaYsyq'), -- kajemsTQ9U4YhRu
       (6, 'username4', 'username4@username4.com', '$2a$10$aT4TbRqT3.RwT9uGwX212eL/ly1MWyiGlOoRiqXWnbj8MXadG.HjW'), -- pOmqK1Ad0F3Fn0E
       (7, 'username5', 'username5@username5.com', '$2a$10$..SWPmnJs4sFhH3o75afG.rMlQ1CFBmvcGItck15vj4nwvZrtOPj.'), -- qtBc4TPpkDxrAYP
       (8, 'username6', 'username6@username6.com', '$2a$10$iUO4OcXg6j6SddJRNWX/POR5uVbWjos8OsxZ9xWJ0/HRBWFIjeHhO'), -- Lb70q7X2dzglZk2
       (9, 'username7', 'username7@username7.com', '$2a$10$8U3lxmmiwi.KGLDhUSr5wOLtPnyYxWrIX/pTwbVUZvo5MYNYKXUdS'), -- 1dz2QeQlDGMYg32
       (10, 'username8', 'username8@username8.com', '$2a$10$lkedbH/56wPYfe.zpqIRXeeBw81QZ/PllkhqbxElJHd6Dj.FB8C3C'); -- TRZ3w78j6pAcyf8

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