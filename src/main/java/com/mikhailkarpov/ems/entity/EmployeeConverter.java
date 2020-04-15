package com.mikhailkarpov.ems.entity;

import com.mikhailkarpov.ems.dto.EmployeeDTO;

import java.util.ArrayList;
import java.util.List;

public class EmployeeConverter {

    private EmployeeConverter() {}

    public static EmployeeDTO toDTO(Employee employee) {
        EmployeeDetails details = employee.getDetails();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(details.getFirstName());
        employeeDTO.setLastName(details.getLastName());
        employeeDTO.setEmail(details.getEmail());

        return employeeDTO;
    }

    public static List<EmployeeDTO> toDTOList(Iterable<Employee> employees) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDTOs.add(toDTO(employee));
        }
        return employeeDTOs;
    }

    public static Employee toEntity(EmployeeDTO employeeDTO) {
        String firstName = employeeDTO.getFirstName();
        String lastName = employeeDTO.getLastName();
        String email = employeeDTO.getEmail();
        EmployeeDetails details = new EmployeeDetails(firstName, lastName, email);

        return new Employee(employeeDTO.getId(), details);
    }
}
