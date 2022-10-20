package com.pp.userservice.lecture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pp.userservice.IntegrationBaseTest;
import org.junit.jupiter.api.Test;

class LectureControllerTest extends IntegrationBaseTest {

    @Test
    void getLectures_ReturnsOK() throws Exception {

        // given
        var url = createUrl();
        var filePath = "data/responses/lectures/getlectures.json";

        // when
        var contentAsString = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        // then
        responseAssertions.assertJSON(contentAsString, filePath);
    }

    @Test
    void getLecturesDetails_ReturnsOK() throws Exception {

        // given
        var url = createUrl("/details");
        var filePath = "data/responses/lectures/getLecturesDetails.json";

        // when
        var contentAsString = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        // then
        responseAssertions.assertJSON(contentAsString, filePath);
    }

    private String createUrl(){

        return LectureController.LECTURES_ENDPOINT;
    }

    private String createUrl(String url){

        return LectureController.LECTURES_ENDPOINT + "/" + url;
    }
}
