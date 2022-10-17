package com.pp.lecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.common.ResponseAssertions;
import com.pp.userservice.lecture.LectureRepository;
import com.pp.userservice.lecture.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SqlGroup({ //
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = LectureControllerTest.CREATE_AND_FILL_SQL_USER_SERVICE), //
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = LectureControllerTest.DROP_SQL_USER_SERVICE) //
})
class LectureControllerTest {

  static final String CREATE_AND_FILL_SQL_USER_SERVICE = "classpath:CREATE_AND_FILL_SQL_USER_SERVICE.sql";
  static final String DROP_SQL_USER_SERVICE = "classpath:DROP_SQL_USER_SERVICE.sql";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ResponseAssertions responseAssertions;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private LectureRepository lectureRepository;
  @Autowired
  private LectureService lectureService;

//  @Test
//  void getLectures_ReturnsOK() throws Exception {
//
//    // given
//    var url = LectureController.LECTURES_ENDPOINT;
//    var filePath = "filePath";
//
//    // when
//    var contentAsString = mockMvc.perform(get(url))
//        .andExpect(status().isOk())
//        .andReturn();
//
//    // then
//    responseAssertions.assertJSON(contentAsString, filePath);
//  }

  @Test
  void getLectures_ReturnNumber() throws Exception {

    var lectures = lectureRepository.findAll()
        .size();
    assertEquals(9, lectures);
  }
}
