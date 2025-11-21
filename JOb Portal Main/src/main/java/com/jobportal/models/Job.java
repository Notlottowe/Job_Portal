package com.jobportal.models;

public class Job {
    private int id;
    private int employerId;
    private String title;
    private String company;
    private String location;
    private double salary;
    private String type;
    private String description;

    public Job() {}

    public Job(int employerId, String title, String company, String location,
               double salary, String type, String description) {
        this.employerId = employerId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.type = type;
        this.description = description;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getEmployerId() { return employerId; }

    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getSalary() { return salary; }

    public void setSalary(double salary) { this.salary = salary; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return title + " - " + company + " (" + location + ")";
    }
}
