package com.example.studentsmanager.bean;

import com.google.gson.Gson;

import java.util.Objects;

public class Student {
    private String id;
    private String name;
    private String dob;
    private String email;
    private String address;

    public static final String tableName = "students";

    public Student(String id, String name, String dob, String email, String address) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getDbName() {
        return tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Student fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Student.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return Objects.equals(id, student.id);
    }


    public boolean update(Student temp) {
        if (!this.equals(temp)) return false;
        if (temp.getName() != null && !"".equals(temp.getName())) {
            this.setName(temp.getName());
        }
        if (temp.getEmail() != null && !"".equals(temp.getEmail())) {
            this.setEmail(temp.getEmail());
        }
        if (temp.getDob() != null && !"".equals(temp.getDob())) {
            this.setDob(temp.getDob());
        }
        if (temp.getAddress() != null && !"".equals(temp.getAddress())) {
            this.setAddress(temp.getAddress());
        }
        return true;
    }

    public boolean updateFromJson(String json) {
        Gson gson = new Gson();
        Student temp = gson.fromJson(json, Student.class);
        return this.update(temp);
    }
}
