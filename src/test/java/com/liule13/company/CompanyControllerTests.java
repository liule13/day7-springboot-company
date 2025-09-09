package com.liule13.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liule13.company.controller.CompanyController;
import com.liule13.company.entity.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void clear() {
        companyController.clear(); // 每次测试前清空数据
    }
    @Test
    void should_return_all_companies_when_list_companies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/companies")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0)); // 初始应为空
    }


}
