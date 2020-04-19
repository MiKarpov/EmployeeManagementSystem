package com.mikhailkarpov.ems.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class EmployeeDetails {

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 20)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 20)
    private String lastName;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    protected EmployeeDetails() {
        // for JPA
    }

    public EmployeeDetails(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "EmployeeDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
