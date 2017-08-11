package ru.frosteye.beermap.data.pojo;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by oleg on 25.07.17.
 */

public class RegisterPackage implements Serializable {

    public static final String KEY = "register_package";

    private int gender;
    private String firstName;
    private String lastName;
    private String avatarPath;

    @Inject
    public RegisterPackage() {
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public int isGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean validate() {
        return gender != 0
                && firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty();
    }
}
