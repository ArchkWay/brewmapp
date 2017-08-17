package ru.frosteye.beermap.data.entity.container;

import java.util.List;

import ru.frosteye.beermap.data.entity.SocialContact;

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
