package com.jobportal;

public class Job {
    private int id;
    private int employerId;
    private String title;
    private String company;
    private String description;
    private String location;
    private double salary;
    private String type;

    public Job() {}

    public Job(int id, int employerId, String title, String company,
               String description, String location, double salary, String type) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.company = company;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.type = type;
    }

    public Job(int employerId, String title, String company,
               String description, String location, double salary, String type) {
        this(0, employerId, title, company, description, location, salary, type);
    }

    public int getId() { return id; }
    public int getEmployerId() { return employerId; }

    public void setId(int id) { this.id = id; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
