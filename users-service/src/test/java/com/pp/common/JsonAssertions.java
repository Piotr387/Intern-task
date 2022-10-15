package com.pp.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UtilityClass
class JsonAssertions {

    static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    void assertEquals(String given, String expected) {

        var givenTree = MAPPER.readTree(given);
        var expectedTree = MAPPER.readTree(expected);
        assertThat(givenTree).isEqualTo(expectedTree);
    }
}
