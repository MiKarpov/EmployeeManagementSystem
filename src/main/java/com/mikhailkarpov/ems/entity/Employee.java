package com.mikhailkarpov.ems.entity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity(name = "Employee")
@Table(name = "employees")
public class Employee extends AbstractEntity {

    @Embedded
    @Valid
    private EmployeeDetails details;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @NotNull
    private Role role;

    protected Employee() {
        // for JPA
    }

    public Employee(Long id, EmployeeDetails details, Role role) {
        this.id = id;
        this.details = details;
        this.role = role;
    }

    public EmployeeDetails getDetails() {
        return details;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", role=" + role.getLabel() +
                ", userDetails=" + details +
                '}';
    }
}
