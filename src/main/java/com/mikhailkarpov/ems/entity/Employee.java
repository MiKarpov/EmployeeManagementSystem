package com.mikhailkarpov.ems.entity;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends AbstractEntity {

    @Embedded
    private EmployeeDetails details;

    protected Employee() {

    }

    public Employee(Long id, EmployeeDetails details) {
        this.id = id;
        this.details = details;
    }

    public Employee(EmployeeDetails details) {
        this(null, details);
    }

    public EmployeeDetails getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userDetails=" + details +
                '}';
    }
}
