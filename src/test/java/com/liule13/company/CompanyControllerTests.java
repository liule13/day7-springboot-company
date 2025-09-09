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
    @Test
    void should_return_company_when_get_company_with_id_existed() throws Exception {
        Company saved = companyController.createCompany(new Company(null, "Spring Corp"));

        mockMvc.perform(MockMvcRequestBuilders.get("/companies/" + saved.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Spring Corp"));
    }
    @Test
    void should_return_paginated_companies_when_list_with_page_and_size() throws Exception {
        for (int i = 1; i <= 6; i++) {
            companyController.createCompany(new Company(null, "Company " + i));
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/companies?page=1&size=5")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
        mockMvc.perform(MockMvcRequestBuilders.get("/companies?page=2&size=5")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void should_create_company_when_post_valid_company() throws Exception {
        Company newCompany = new Company(null, "Tech Inc");

        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Tech Inc"));
    }
    @Test
    void should_update_company_name_when_put_existing_company() throws Exception {
        Company created = companyController.createCompany(new Company(null, "Old Name"));
        Company updatedData = new Company(null, "New Name");

        mockMvc.perform(MockMvcRequestBuilders.put("/companies/" + created.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.name").value("New Name"));
    }

}
