package com.example.GreenRidersHBTU.Model;

public class Student {
// DUMMY STUDENT OBJECT AS PER MONGODB SCHEMA
   /* {
        "_id": "642f12f26993b0c7248a9a35",
            "email": "200106069@hbtu.ac.in",
            "password": "$2b$10$5LjJhh3EhBDsb5oubvYDFuNsRncP/SnPcEa6AezinyUzCDzomEXS.",
            "name": "SURYANSH SRIVASTAVA ",
            "rollno": "200106069",
            "branch": "Electronics Engineering",
            "cycleid": "HBTUCycle110",
            "role": "student",
            "__v": 0
    }*/
    String name, branch, email, rollno, cycleid;

    public Student(String stdName, String stdBranch, String stdEmail, String stdRollNo, String stdCycleId) {
        this.name = stdName;
        this.branch = stdBranch;
        this.email = stdEmail;
        this.rollno = stdRollNo;
        this.cycleid = stdCycleId;
    }
    public String getAllDetails() {
        return "Name : "+name + "\nBranch : " + branch + "\nEmail : " + email + "\nRoll No : " + rollno + "\nCycleId : " + cycleid;
    }

        public String getStdName() {
        return name;
    }

    public void setStdName(String stdName) {
        this.name = stdName;
    }

    public String getStdBranch() {
        return branch;
    }

    public void setStdBranch(String stdBranch) {
        this.branch = stdBranch;
    }

    public String getStdEmail() {
        return email;
    }

    public void setStdEmail(String stdEmail) {
        this.email = stdEmail;
    }

    public String getStdRollNo() {
        return rollno;
    }

    public void setStdRollNo(String stdRollNo) {
        this.rollno = stdRollNo;
    }

    public String getStdCycleId() {
        return cycleid;
    }

    public void setStdCycleId(String stdCycleId) {
        this.cycleid = stdCycleId;
    }
}
