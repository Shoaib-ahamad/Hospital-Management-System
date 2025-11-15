package com.hospital.model;

import java.util.Date;

public class AppointmentRequest {
    private int requestId;
    private int patientId;
    private String specialization;
    private Date requestedDate;
    private String status; // PENDING, APPROVED, REJECTED
    private String patientName;
    private String description;

    public AppointmentRequest() {
    }

    public AppointmentRequest(int requestId, int patientId, String specialization, Date requestedDate, String status) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.specialization = specialization;
        this.requestedDate = requestedDate;
        this.status = status;
    }

    public AppointmentRequest(int patientId, String specialization, Date requestedDate, String status) {
        this.patientId = patientId;
        this.specialization = specialization;
        this.requestedDate = requestedDate;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AppointmentRequest{" +
                "requestId=" + requestId +
                ", patientId=" + patientId +
                ", specialization='" + specialization + '\'' +
                ", requestedDate=" + requestedDate +
                ", status='" + status + '\'' +
                ", patientName='" + patientName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

