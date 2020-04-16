package com.mikhailkarpov.ems.service;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Employee;
import com.mikhailkarpov.ems.entity.EmployeeDetails;
import com.mikhailkarpov.ems.exception.EntityNotFoundException;
import com.mikhailkarpov.ems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> findAll() {
        LOGGER.debug("Fetching all employees");
        Iterable<Employee> employees = employeeRepository.findAll();
        return toDTO(employees);
    }

    public List<EmployeeDTO> search(String searchQuery) {
        LOGGER.debug("Searching employees: {}", searchQuery);
        List<Employee> employees = employeeRepository.searchBy(searchQuery);
        return toDTO(employees);
    }

    public EmployeeDTO findById(Long id) {
        LOGGER.debug("Searching for employee with id=" + id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id=" + id));
        return toDTO(employee);
    }

    public void save(EmployeeDTO employee) {
        LOGGER.debug("Saving {}", employee);
        employeeRepository.save(toEntity(employee));
    }

    public void deleteById(Long id) {
        LOGGER.debug("Deleting employee by id=" + id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id=" + id));
        employeeRepository.delete(employee);
    }

    private EmployeeDTO toDTO(Employee employee) {
        EmployeeDetails details = employee.getDetails();
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setRole(employee.getRole());
        employeeDTO.setFirstName(details.getFirstName());
        employeeDTO.setLastName(details.getLastName());
        employeeDTO.setEmail(details.getEmail());

        return employeeDTO;
    }

    private List<EmployeeDTO> toDTO(Iterable<Employee> employees) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDTOs.add(toDTO(employee));
        }
        return employeeDTOs;
    }

    private Employee toEntity(EmployeeDTO employeeDTO) {
        String firstName = employeeDTO.getFirstName();
        String lastName = employeeDTO.getLastName();
        String email = employeeDTO.getEmail();
        EmployeeDetails details = new EmployeeDetails(firstName, lastName, email);

        return new Employee(employeeDTO.getId(), details, employeeDTO.getRole());
    }
}