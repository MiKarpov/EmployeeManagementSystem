package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.entity.Employee;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static com.mikhailkarpov.ems.entity.Role.PROJECT_MANAGER;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    public void whenGetAllEmployees_thenRenderEmployeeList() throws Exception {
        MockHttpServletRequestBuilder getAllEmployees = get("/employee")
                .param("page", "1")
                .param("size", "3");

        mockMvc.perform(getAllEmployees)
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employeePage", iterableWithSize(2)))
                .andExpect(model().attribute("pageNumbers", hasSize(2)))
                .andExpect(model().attributeDoesNotExist("searchKeyword"));
    }

    @Test
    public void whenSearchEmployee_thenRenderSearchResults() throws Exception {
        MockHttpServletRequestBuilder searchEmployee = get("/employee")
                .param("searchKeyword", "ov")
                .param("size", "3");

        mockMvc.perform(searchEmployee)
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employeePage", iterableWithSize(3)))
                .andExpect(model().attribute("pageNumbers", hasSize(2)))
                .andExpect(model().attribute("searchKeyword", is("ov")));
    }

    @Test
    @Transactional
    public void givenNewEmployee_whenSave_thenSaveAndRenderEmployeeList() throws Exception {
        MockHttpServletRequestBuilder saveNewEmployee = post("/employee")
                .param("firstName", "First Name")
                .param("lastName", "Last Name")
                .param("email", "Email")
                .param("role", String.valueOf(PROJECT_MANAGER));

        mockMvc.perform(saveNewEmployee)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"));

        String query = "SELECT e FROM Employee e WHERE e.details.firstName = :firstName";
        List<Employee> resultList = entityManager.createQuery(query)
                .setParameter("firstName", "First Name")
                .getResultList();

        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());

        Employee employee = resultList.get(0);

        assertNotNull(employee.getId());
        assertEquals("First Name", employee.getDetails().getFirstName());
        assertEquals("Last Name", employee.getDetails().getLastName());
        assertEquals("Email", employee.getDetails().getEmail());
        assertEquals(PROJECT_MANAGER, employee.getRole());
    }

    @Test
    @Transactional
    public void givenExistingEmployee_whenSave_thenSaveAndRenderEmployeeList() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/employee")
                .param("id", "1")
                .param("firstName", "Updated First Name")
                .param("lastName", "Updated Last Name")
                .param("email", "Updated Email")
                .param("role", String.valueOf(PROJECT_MANAGER));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"));

        String query = "SELECT e FROM Employee e WHERE e.id = :id";
        List<Employee> resultList = entityManager.createQuery(query)
                .setParameter("id", 1L)
                .getResultList();

        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());

        Employee employee = resultList.get(0);

        assertNotNull(employee.getId());
        assertEquals("Updated First Name", employee.getDetails().getFirstName());
        assertEquals("Updated Last Name", employee.getDetails().getLastName());
        assertEquals("Updated Email", employee.getDetails().getEmail());
        assertEquals(PROJECT_MANAGER, employee.getRole());
    }

    @Test
    public void whenShowSaveOrEditEmployeeForm_withNoId_thenShowSaveForm() throws Exception {
        MockHttpServletRequestBuilder showSaveForm = get("/employee/save");

        mockMvc.perform(showSaveForm)
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employee", hasProperty("id", nullValue())))
                .andExpect(model().attribute("employee", hasProperty("firstName", nullValue())))
                .andExpect(model().attribute("employee", hasProperty("lastName", nullValue())))
                .andExpect(model().attribute("employee", hasProperty("email", nullValue())))
                .andExpect(model().attribute("employee", hasProperty("role", nullValue())))
                .andExpect(model().attributeExists("allRoles"));
    }

    @Test
    public void whenShowSaveOrEditEmployeeForm_withId_thenShowEditForm() throws Exception {
        mockMvc.perform(get("/employee/save?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("employee", hasProperty("id", notNullValue())))
                .andExpect(model().attribute("employee", hasProperty("firstName", notNullValue())))
                .andExpect(model().attribute("employee", hasProperty("lastName", notNullValue())))
                .andExpect(model().attribute("employee", hasProperty("email", notNullValue())))
                .andExpect(model().attribute("employee", hasProperty("role", notNullValue())))
                .andExpect(model().attributeExists("allRoles"));
    }

    @Test
    @Disabled
    public void givenId_IdNotFound_whenGetEmployee_thenRender404() throws Exception {
        //todo add exception handler
    }

    @Test
    @Transactional
    public void givenId_whenDeleteEmployee_thenDeleteEmployeeAndRenderEmployeeList() throws Exception {
        Query selectQuery = entityManager.createQuery("SELECT e FROM Employee e WHERE e.id = :id")
                .setParameter("id", 1L);
        assertFalse(selectQuery.getResultList().isEmpty());

        MockHttpServletRequestBuilder deleteEmployee = post("/employee/delete")
                .param("id", "1");

        mockMvc.perform(deleteEmployee)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employee"))
                .andExpect(model().hasNoErrors());

        assertTrue(selectQuery.getResultList().isEmpty());
    }
}