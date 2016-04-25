package com.example.brian.cleverrent;

/**
 * Created by brian on 3/28/16.
 */
public class MaintenanceRequest {
    private String tenantName;
    private String tenantAddress;
    private String requestType;
    private String description;
    private String timeForService;
    private String timeOfSubmission;
    private String status;
    private String responseTime;

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

    public String getRequestType() {
        return requestType;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeForService() {
        return timeForService;
    }

    public String getResponseTime() {return responseTime;}

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void setTenantAddress(String tenantAddress) {
        this.tenantAddress = tenantAddress;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeForService(String timeForService) {
        this.timeForService = timeForService;
    }



    @Override
    public boolean equals(Object o) {
        if (o instanceof MaintenanceRequest){
            MaintenanceRequest temp = (MaintenanceRequest) o;
            if (temp.getTimeOfSubmission().equals(this.getTimeOfSubmission())){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
