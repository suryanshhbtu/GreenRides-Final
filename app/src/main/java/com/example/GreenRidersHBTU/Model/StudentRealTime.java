package com.example.GreenRidersHBTU.Model;

public class StudentRealTime {
    String stdName, stdBranch, stdEmail, stdRollNo, stdCycleId;

    public StudentRealTime(String stdName, String stdBranch, String stdEmail, String stdRollNo, String stdCycleId) {
        this.stdName = stdName;
        this.stdBranch = stdBranch;
        this.stdEmail = stdEmail;
        this.stdRollNo = stdRollNo;
        this.stdCycleId = stdCycleId;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getStdBranch() {
        return stdBranch;
    }

    public void setStdBranch(String stdBranch) {
        this.stdBranch = stdBranch;
    }

    public String getStdEmail() {
        return stdEmail;
    }

    public void setStdEmail(String stdEmail) {
        this.stdEmail = stdEmail;
    }

    public String getStdRollNo() {
        return stdRollNo;
    }

    public void setStdRollNo(String stdRollNo) {
        this.stdRollNo = stdRollNo;
    }

    public String getStdCycleId() {
        return stdCycleId;
    }

    public void setStdCycleId(String stdCycleId) {
        this.stdCycleId = stdCycleId;
    }
}
