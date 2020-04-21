package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.exception.EntityNotFoundException;
import com.mikhailkarpov.ems.service.EmployeeService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private Page mockPage = Mockito.mock(Page.class);

    @Test
    public void whenGetAllEmployees_thenRenderEmployeeList() throws Exception {
        when(employeeService.findAllPaginated(PageRequest.of(1, 3)))
                .thenReturn(mockPage);
        when(mockPage.getTotalPages()).thenReturn(2);

        MockHttpServletRequestBuilder getAllEmployees = get("/employee")
                .param("page", "1")
                .param("size", "3");

        mockMvc.perform(getAllEmployees)
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employeePage", is(mockPage)))
                .andExpect(model().attribute("pageNumbers", hasSize(2)))
                .andExpect(model().attributeDoesNotExist("searchKeyword"));
    }

    @Test
    public void whenSearchEmployee_thenRenderSearchResults() throws Exception {
        when(employeeService.findAllPaginated("ov", PageRequest.of(2, 3)))
                .thenReturn(mockPage);
        when(mockPage.getTotalPages()).thenReturn(2);

        MockHttpServletRequestBuilder searchEmployee = get("/employee")
                .param("searchKeyword", "ov")
                .param("page", "2")
                .param("size", "3");

        mockMvc.perform(searchEmployee)
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employeePage", is(mockPage)))
                .andExpect(model().attribute("pageNumbers", hasSize(2)))
                .andExpect(model().attribute("searchKeyword", is("ov")));
    }

    @Test
    public void givenId_IdNotFound_whenGetEmployee_thenRender404() throws Exception {
        when(employeeService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/employee/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void givenId_whenDeleteEmployee_thenDeleteEmployeeAndRenderEmployeeList() throws Exception {
        MockHttpServletRequestBuilder deleteEmployee = post("/employee/delete")
                .param("id", "1");

        mockMvc.perform(deleteEmployee)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"))
                .andExpect(model().hasNoErrors());

    }
}