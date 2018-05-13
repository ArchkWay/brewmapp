package com.brewmapp.data.pojo;

import com.brewmapp.data.entity.User;

import java.io.Serializable;

/**
 * Created by ovcst on 03.08.2017.
 */

public class RegisterPackageWithPhone extends RegisterPackage implements Serializable {

    private String phone;

    public RegisterPackageWithPhone(RegisterPackage registerPackage) {
        setFirstName(registerPackage.getFirstName());
        setLastName(registerPackage.getLastName());
        setGender(registerPackage.getGender());
        setAvatarPath(registerPackage.getAvatarPath());
        setEmail(registerPackage.getEmail());
    }

    public RegisterPackageWithPhone(User user) {
        setPhone(user.getPhone());
        setFirstName(user.getFirstname());
        setLastName(user.getLastname());
        setGender(user.getGender());
        setEmail(user.getEmail());
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean validate() {
        return super.validate() && phone != null && phone.length() == 11;
    }
}
