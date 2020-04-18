package com.mikhailkarpov.ems.service;

import com.mikhailkarpov.ems.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeDTO> findAllPaginated(String searchKeyword, Pageable pageable);

    Page<EmployeeDTO> findAllPaginated(Pageable pageable);

    EmployeeDTO findById(Long id);

    void save(EmployeeDTO employee);

    void deleteById(Long id);
}
