package org.pattonvillecs.pattonvilleapp;

/**
 * Created by gadsonk on 12/7/16.
 */

public class Faculty {

    private String mName;
    private String mDepartment;
    private String mEmail;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    //dummy data for now
    public Faculty setFaculty() {
        setName("First Name Last Name...");
        setDepartment("Department...");
        setEmail("Email");
        return this;
    }
}
