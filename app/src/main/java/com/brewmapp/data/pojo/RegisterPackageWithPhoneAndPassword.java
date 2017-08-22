package com.brewmapp.data.pojo;

import java.io.Serializable;

/**
 * Created by ovcst on 03.08.2017.
 */

public class RegisterPackageWithPhoneAndPassword extends RegisterPackageWithPhone implements Serializable {

    private String password;

    public RegisterPackageWithPhoneAndPassword(RegisterPackageWithPhone registerPackage) {
        super(registerPackage);
        setPhone(registerPackage.getPhone());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean validate() {
        return super.validate() && password != null && password.length() > 3;
    }
}
