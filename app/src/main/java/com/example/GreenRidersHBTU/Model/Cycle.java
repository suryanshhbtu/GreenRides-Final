package com.example.GreenRidersHBTU.Model;

public class Cycle {
    String _id;
    String cycleid;
    String status;
    String stdid;
    String email;
    String stdname;

    public String getEmail() {
        return email;
    }

    public String getStdname() {
        return stdname;
    }
    public String getRollNo() {
        return email.substring(0,9);
    }

    public String getBranchName() {
        String branch = "";
        String curr = email.substring(4,6);
        switch(curr){
            case "01":
                branch = "BE";
                break;
            case "02":
                branch = "Civil Engineering";
                break;
            case "03":
                branch = "Chemical Engineering";
                break;
            case "04":
                branch = "Computer Science Engineering";
                break;
            case "05":
                branch = "Electrical Engineering";
                break;
            case "06":
                branch = "Electronics Engineering";
                break;
            case "07":
                branch = "Food Technology";
                break;
            case "08":
                branch = "Information Technology";
                break;
            case "09":
                branch = "Leather Technology";
                break;
            case "10":
                branch = "Mechanical Engineering";
                break;
            case "11":
                branch = "Oil Technology";
                break;
            case "12":
                branch = "Paint And Leather";
                break;
            case "13":
                branch = "Paint Technology";
                break;
        }
        return branch;
    }

    public String get_id() {
        return _id;
    }

    public String getCycleid() {
        return cycleid;
    }

    public String getStatus() {
        return status;
    }

    public String getStdid() {
        return stdid;
    }
}
