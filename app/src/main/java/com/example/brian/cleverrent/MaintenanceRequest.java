package com.example.brian.cleverrent;

/**
 * Created by brian on 3/28/16.
 */
public class MaintenanceRequest {
    private String tenantName;
    private String tenantAddress;
    private String maintenanceType;
    private String description;
    private String timeForService;
    private String timeOfSubmission;
    private String status;

    public MaintenanceRequest() {}

    public String getTimeOfSubmission() {
        return timeOfSubmission;
    }

    public void setTimeOfSubmission(String timeOfSubmission) {
        this.timeOfSubmission = timeOfSubmission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getTenantAddress() {
        return tenantAddress;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeForService() {
        return timeForService;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void setTenantAddress(String tenantAddress) {
        this.tenantAddress = tenantAddress;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeForService(String timeForService) {
        this.timeForService = timeForService;
    }
}
