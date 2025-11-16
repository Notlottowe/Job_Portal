package com.jobportal;

public class Application {
    private int id;
    private int jobId;
    private int employeeId;
    private String employeeName;
    private String employeeEmail;

    public Application(int id, int jobId, int employeeId, String employeeName, String employeeEmail) {
        this.id = id;
        this.jobId = jobId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
    }

    public int getId() { return id; }
    public int getJobId() { return jobId; }
    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getEmployeeEmail() { return employeeEmail; }
}
