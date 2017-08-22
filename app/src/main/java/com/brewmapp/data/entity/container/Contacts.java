package com.brewmapp.data.entity.container;

import java.util.List;

import com.brewmapp.data.entity.SocialContact;

/**
 * Created by oleg on 17.08.17.
 */

public class Contacts {
    private List<SocialContact> contacts;

    public Contacts(List<SocialContact> contacts) {
        this.contacts = contacts;
    }

    public List<SocialContact> getContacts() {
        return contacts;
    }
}
