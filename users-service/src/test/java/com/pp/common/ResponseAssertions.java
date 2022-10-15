package com.pp.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

@Component
@RequiredArgsConstructor
public class ResponseAssertions {

    private final FileUtilities fileUtilities;

    public void assertJSON(MvcResult result, String pathToFileWithJSON) throws Exception {

        String body = result.getResponse().getContentAsString();

        var expectedResponseBody = fileUtilities.readFile(pathToFileWithJSON);
        JsonAssertions.assertEquals(body, expectedResponseBody);
    }
}
