package com.jobportal;

import java.time.LocalDate;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String role; // "Employee" or "Employer"

    private LocalDate birthdate;
    private String description;
    private String experience;
    private String education;
    private String skills;

    public User() {}

    public User(int id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public boolean isEmployer() {
        return "Employer".equalsIgnoreCase(role);
    }

    public boolean isEmployee() {
        return "Employee".equalsIgnoreCase(role);
    }
}
