package com.mikhailkarpov.ems.repository;

import com.mikhailkarpov.ems.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Query("select e from Employee e " +
            "where e.details.firstName like %?1% " +
            "or e.details.lastName like %?1% " +
            "or e.details.email like %?1%")
    List<Employee> searchBy(String query);

}
