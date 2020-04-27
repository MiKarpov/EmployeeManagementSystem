package com.mikhailkarpov.ems.controller;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Role;
import com.mikhailkarpov.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(@RequestParam(required = false) String searchKeyword,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size,
                                  Model model) {
        Pageable pageable = getPageable(page, size);

        Page<EmployeeDTO> employeePage;
        if (searchKeyword != null) {
            String trimmed = searchKeyword.trim();
            employeePage = employeeService.findAllPaginated(trimmed, pageable);
            model.addAttribute("searchKeyword", trimmed);
        }
        else {
            employeePage = employeeService.findAllPaginated(pageable);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("allRoles", Role.ALL_ROLES);

        int totalPages = employeePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, totalPages + 1).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "employee-list";
    }

    private Pageable getPageable(Integer page, Integer size) {
        int currentPage = 0;
        int currentSize = DEFAULT_PAGE_SIZE;

        if (page != null && page >= 0) {
            currentPage = page;
        }
        if (size != null && size > 0) {
            currentSize = size;
        }
        return PageRequest.of(currentPage, currentSize);
    }

    @GetMapping("/getEmployee")
    @ResponseBody
    public EmployeeDTO getEmployee(Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute EmployeeDTO employee,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("alertClass", "danger");
            //todo front-end validation
            model.addAttribute("alertMsg", "User not saved due to errors");
            return "employee";
        }

        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("alertClass", "success");
        redirectAttributes.addFlashAttribute("alertMsg", "User saved");
        return "redirect:/employee";
    }

    @PostMapping(value = "/delete")
    public String deleteEmployee(@RequestParam Long id, final RedirectAttributes redirectAttributes) {
        employeeService.deleteById(id);
        redirectAttributes.addFlashAttribute("alertClass", "success");
        redirectAttributes.addFlashAttribute("alertMsg", "User deleted");
        return "redirect:/employee";
    }

}