package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.dto.Role;
import com.mikhailkarpov.ems.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(@RequestParam (required = false) String searchQuery, Model model) {
        List<EmployeeDTO> employees;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            searchQuery = searchQuery.trim();
            employees = employeeService.search(searchQuery);
            model.addAttribute("searchQuery", searchQuery);
        }
        else {
            employees = employeeService.findAll();
        }

        model.addAttribute("employees", employees);
        return "employee-list";
    }

    @PostMapping
    public String saveEmployee(@ModelAttribute EmployeeDTO employee) {
        employeeService.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("/{id}")
    public String getEmployee(@PathVariable Long id, Model model) {
        EmployeeDTO employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "employee";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "employee";
    }

    @PostMapping(value = "/deleteBy", params = {"id"})
    public String deleteEmployeeById(@RequestParam(required = true) Long id) {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }

    @ModelAttribute
    public void populateRoles(Model model) {
        model.addAttribute("allRoles", Arrays.asList(Role.ALL));
    }
}