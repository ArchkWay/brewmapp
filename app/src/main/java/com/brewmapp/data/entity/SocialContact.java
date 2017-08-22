package com.brewmapp.data.entity;

import android.net.Uri;

/**
 * Created by oleg on 17.08.17.
 */

public class SocialContact {
    private String firstName;
    private String lastName;
    private int origin;
    private String pictureUrl;
    private String socialId;
    private String phone;
    private String email;
    private Uri localPictureUri;

    private transient boolean inviteSent;

    public String getFormattedName() {
        return String.format("%s %s", firstName, lastName).replace("null","").trim();
    }

    public boolean isInviteSent() {
        return inviteSent;
    }

    public void setInviteSent(boolean inviteSent) {
        this.inviteSent = inviteSent;
    }

    public Uri getLocalPictureUrl() {
        return localPictureUri;
    }

    public void setLocalPictureUrl(Uri localPictureUrl) {
        this.localPictureUri = localPictureUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
