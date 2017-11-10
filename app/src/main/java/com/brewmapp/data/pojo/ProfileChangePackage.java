package com.brewmapp.data.pojo;

import android.text.TextUtils;

/**
 * Created by Kras on 10.11.2017.
 */

public class ProfileChangePackage {
    private String firstName;
    private String lastName;

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean isNeedSave() {
        if(TextUtils.isEmpty(firstName)||TextUtils.isEmpty(lastName))
            return false;

        return true;
    }
}
