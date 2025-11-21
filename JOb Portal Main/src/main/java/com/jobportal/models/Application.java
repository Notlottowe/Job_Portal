package com.jobportal.models;

public class Application {
    private int id;
    private int jobId;
    private int employeeId;
    private String jobTitle;
    private String company;
    private String location;
    private String type;
    private String coverLetter;

    public Application() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }

    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getEmployeeId() { return employeeId; }

    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getJobTitle() { return jobTitle; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getCoverLetter() { return coverLetter; }

    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
}
