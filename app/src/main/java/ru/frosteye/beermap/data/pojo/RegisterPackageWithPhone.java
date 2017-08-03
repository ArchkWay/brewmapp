package ru.frosteye.beermap.data.pojo;

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
