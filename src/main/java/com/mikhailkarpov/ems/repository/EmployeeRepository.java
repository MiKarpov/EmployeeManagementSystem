package com.mikhailkarpov.ems.repository;

import com.mikhailkarpov.ems.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

    @Query("select e from Employee e " +
            "where e.details.firstName like %?1% " +
            "or e.details.lastName like %?1% " +
            "or e.details.email like %?1%")
    Page<Employee> searchBy(String query, Pageable pageable);

}
