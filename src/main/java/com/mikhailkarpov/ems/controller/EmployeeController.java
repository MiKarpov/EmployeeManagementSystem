package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Role;
import com.mikhailkarpov.ems.service.EmployeeServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private static final int PAGE_SIZE = 10;
    private EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @Deprecated
    @GetMapping
    public String getAllEmployees(@RequestParam Optional<String> searchKeyword,
                                  @RequestParam Optional<Integer> page,
                                  @RequestParam Optional<Integer> size,
                                  Model model) {
        Pageable pageable = getPageable(page, size);

        Page<EmployeeDTO> employeePage;
        if (searchKeyword.isPresent()) {
            String trimmed = searchKeyword.get().trim();
            employeePage = employeeService.findAllPaginated(trimmed, pageable);
            model.addAttribute("searchKeyword", trimmed);
        }
        else {
            employeePage = employeeService.findAllPaginated(pageable);
        }

        model.addAttribute("employeePage", employeePage);

        int totalPages = employeePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "employee-list";
    }

    private Pageable getPageable(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = 0;
        int currentSize = PAGE_SIZE;

        if (page.isPresent() && page.get() >= 0) {
            currentPage = page.get();
        }
        if (size.isPresent() && size.get() > 0) {
            currentSize = size.get();
        }
        return PageRequest.of(currentPage, currentSize);
    }

    @GetMapping("/save")
    public String showSaveOrEditEmployeeForm(@RequestParam Optional<Long> id, Model model) {
        EmployeeDTO employee;
        if (id.isPresent()) {
            employee = employeeService.findById(id.get());
        }
        else {
            employee = new EmployeeDTO();
        }
        model.addAttribute("employee", employee);
        model.addAttribute("allRoles", Role.ALL_ROLES);
        return "employee";
    }

    @PostMapping
    public String saveEmployee(@ModelAttribute EmployeeDTO employee) {
        employeeService.save(employee);
        return "redirect:/employee";
    }

//    @GetMapping("/{id}")
//    public String getEmployee(@PathVariable Long id, Model model) {
//        EmployeeDTO employee = employeeService.findById(id);
//        model.addAttribute("employee", employee);
//        return "employee";
//    }

    @PostMapping(value = "/delete", params = {"id"})
    public String deleteEmployee(@RequestParam Long id) {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }

//    @ModelAttribute
//    public void populateNewEmployee(Model model) {
//        model.addAttribute("newEmployee", new EmployeeDTO());
//    }

//    @ModelAttribute
//    public void populateRoles(Model model) {
//        model.addAttribute("allRoles", Arrays.asList(Role.ALL_ROLES));
//    }
}