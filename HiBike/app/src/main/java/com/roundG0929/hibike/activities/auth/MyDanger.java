package com.roundG0929.hibike.activities.auth;

public class MyDanger {
    int dangerId;
    String title;
    String region;
    String time;
    String isDelete;

    public MyDanger(int dangerId, String title, String region, String time, String isDelete) {
        this.dangerId = dangerId;
        this.title = title;
        this.region = region;
        this.time = time;
        this.isDelete = isDelete;
    }

    public int getDangerId() {
        return dangerId;
    }

    public void setDangerId(int dangerId) {
        this.dangerId = dangerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }


}
