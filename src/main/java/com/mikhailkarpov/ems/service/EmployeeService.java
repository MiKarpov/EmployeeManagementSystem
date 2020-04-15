package com.mikhailkarpov.ems.service;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Employee;
import com.mikhailkarpov.ems.entity.EmployeeConverter;
import com.mikhailkarpov.ems.exception.EntityNotFoundException;
import com.mikhailkarpov.ems.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
        List<EmployeeDTO> employeeDTOs = EmployeeConverter.toDTOList(employees);
        return Collections.unmodifiableList(employeeDTOs);
    }

    public List<EmployeeDTO> search(String searchQuery) {
        LOGGER.info("Searching employees: {}", searchQuery);
        List<Employee> employees = employeeRepository.searchBy(searchQuery);
        return Collections.unmodifiableList(EmployeeConverter.toDTOList(employees));
    }

    public EmployeeDTO findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id=" + id));
        return EmployeeConverter.toDTO(employee);
    }

    public void save(EmployeeDTO employee) {
        employeeRepository.save(EmployeeConverter.toEntity(employee));
    }

    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id=" + id));
        employeeRepository.delete(employee);
    }
}