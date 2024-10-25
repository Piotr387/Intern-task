package com.pp.userservice;

import com.pp.userservice.common.ResponseAssertions;
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
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = CommonTest.CREATE_AND_FILL_SQL_USER_SERVICE), //
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = CommonTest.DROP_SQL_USER_SERVICE) //
})
public abstract class CommonTest {

    static final String CREATE_AND_FILL_SQL_USER_SERVICE = "classpath:CREATE_AND_FILL_SQL_USER_SERVICE.sql";
    static final String DROP_SQL_USER_SERVICE = "classpath:DROP_SQL_USER_SERVICE.sql";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ResponseAssertions responseAssertions;
}
