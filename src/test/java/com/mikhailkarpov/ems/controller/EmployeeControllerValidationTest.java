package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Role;
import com.mikhailkarpov.ems.repository.EmployeeRepository;
import com.mikhailkarpov.ems.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();

    @Test
    public void givenValidEmployee_whenSaveEmployee_thenHasNoErrors() throws Exception {
        paramsMap.set("firstName", "First Name");
        paramsMap.set("lastName", "Last Name");
        paramsMap.set("email", "email@example.com");
        paramsMap.set("role", String.valueOf(Role.PROJECT_MANAGER));

        MockHttpServletRequestBuilder saveEmployee = post("/employee")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(paramsMap)
                .requestAttr("employee", new EmployeeDTO());

        mockMvc.perform(saveEmployee)
                .andExpect(view().name("redirect:/employee"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void givenValidEmployee_withId_whenSaveEmployee_thenHasNoErrors() throws Exception {
        paramsMap.set("id", "1");
        paramsMap.set("firstName", "First Name");
        paramsMap.set("lastName", "Last Name");
        paramsMap.set("email", "email@example.com");
        paramsMap.set("role", String.valueOf(Role.PROJECT_MANAGER));

        MockHttpServletRequestBuilder saveEmployee = post("/employee")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(paramsMap)
                .requestAttr("employee", new EmployeeDTO());

        mockMvc.perform(saveEmployee)
                .andExpect(view().name("redirect:/employee"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void givenEmployer_withBlankFields_whenSaveEmployee_thenHasErrors() throws Exception {
        paramsMap.set("firstName", "");
        paramsMap.set("lastName", "");
        paramsMap.set("email", "");
        paramsMap.set("role", "");

        MockHttpServletRequestBuilder saveEmployee = post("/employee")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(paramsMap)
                .requestAttr("employee", new EmployeeDTO());

        mockMvc.perform(saveEmployee)
                .andExpect(view().name("employee"))
                .andExpect(model().attributeHasFieldErrors("employee", "firstName", "lastName", "email", "role"));
    }

    @Test
    public void  givenEmployer_withInvalidEmail_whenSave_thenHasErrors() throws Exception {
        paramsMap.set("firstName", "f");
        paramsMap.set("lastName", "l");
        paramsMap.set("email", "e");
        paramsMap.set("role", String.valueOf(Role.PROJECT_MANAGER));

        MockHttpServletRequestBuilder saveEmployee = post("/employee")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(paramsMap)
                .requestAttr("employee", new EmployeeDTO());

        mockMvc.perform(saveEmployee)
                .andExpect(view().name("employee"))
                .andExpect(model().attributeHasFieldErrors("employee", "firstName", "lastName", "email"));
    }

}
