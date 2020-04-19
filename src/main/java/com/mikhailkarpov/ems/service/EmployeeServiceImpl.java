package com.mikhailkarpov.ems.service;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import com.mikhailkarpov.ems.entity.Employee;
import com.mikhailkarpov.ems.entity.EmployeeDetails;
import com.mikhailkarpov.ems.exception.EntityNotFoundException;
import com.mikhailkarpov.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<EmployeeDTO> findAllPaginated(String searchKeyword, Pageable pageable) {
        final String trimmedSearchQuery = searchKeyword.trim();
        if (trimmedSearchQuery.isEmpty())
            return findAllPaginated(pageable);

        return employeeRepository.searchBy(trimmedSearchQuery, pageable).map(this::toDTO);
    }

    @Override
    public Page<EmployeeDTO> findAllPaginated(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public EmployeeDTO findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found by id=" + id));
        return toDTO(employee);
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employee) {
        Employee savedEmployee = employeeRepository.save(toEntity(employee));
        return toDTO(savedEmployee);
    }

    @Override
    public void deleteById(Long id) {
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

    private Employee toEntity(EmployeeDTO employeeDTO) {
        String firstName = employeeDTO.getFirstName();
        String lastName = employeeDTO.getLastName();
        String email = employeeDTO.getEmail();
        EmployeeDetails details = new EmployeeDetails(firstName, lastName, email);

        return new Employee(employeeDTO.getId(), details, employeeDTO.getRole());
    }
}