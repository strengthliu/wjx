package com.ccwihr_server.domain;

public class Departs {
    private String departId;

    private String departName;

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId == null ? null : departId.trim();
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName == null ? null : departName.trim();
    }
}