package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 15.07.17.
 */

public class User {
    private int id;
    private String phone;
    private String code;
    private String token;
    private String firstname;
    private String lastname;
    private String email;
    private String fb;

    @SerializedName(Keys.FB_ACCESS_TOKEN)
    private String fbAccessToken;

    @SerializedName(Keys.FB_EXPIRES)
    private String fbExpires;

    private String image;
    private int gender;
    private Date timestamp;
    private String login;

    @SerializedName(Keys.COUNTRY_ID)
    private int countryId;

    private String role;

    @SerializedName(Keys.CITY_ID)
    private int cityId;

    @SerializedName(Keys.RESTO_WORK_ID)
    private String restoWorkId;

    @SerializedName(Keys.PROFESSION_ID)
    private String professionId;

    private Date birthday;

    @SerializedName(Keys.FAMILY_STATUS)
    private int familyStatus;

    @SerializedName(Keys.MOB_PHONE)
    private String mobilePhone;

    @SerializedName(Keys.ADDIT_PHONE)
    private String additionalPhone;

    private String skype;
    private String site;
    private String job;
    private String interests;
    private String music;
    private String films;
    private String books;
    private String games;
    private String about;
    private String status;

    private Counts counts;

    @SerializedName(Keys.LAST_LOGIN)
    private Date lastLogin;

    @SerializedName(Keys.GET_THUMB)
    private String thumbnail;

    public String getFormattedName() {
        return String.format("%s %s", firstname, lastname);
    }

    public int getId() {
        return id;
    }

    public Counts getCounts() {
        return counts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getFbAccessToken() {
        return fbAccessToken;
    }

    public void setFbAccessToken(String fbAccessToken) {
        this.fbAccessToken = fbAccessToken;
    }

    public String getFbExpires() {
        return fbExpires;
    }

    public void setFbExpires(String fbExpires) {
        this.fbExpires = fbExpires;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getRestoWorkId() {
        return restoWorkId;
    }

    public void setRestoWorkId(String restoWorkId) {
        this.restoWorkId = restoWorkId;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(int familyStatus) {
        this.familyStatus = familyStatus;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAdditionalPhone() {
        return additionalPhone;
    }

    public void setAdditionalPhone(String additionalPhone) {
        this.additionalPhone = additionalPhone;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getFilms() {
        return films;
    }

    public void setFilms(String films) {
        this.films = films;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static class Counts {
        @SerializedName(Keys.CAP_NEWS)
        private int news;

        @SerializedName(Keys.CAP_PHOTO_ALBUM)
        private int albums;

        @SerializedName(Keys.CAP_PHOTO)
        private int photos;

        @SerializedName(Keys.CAP_SIGNED_AT_USER)
        private int subscribers;

        @SerializedName(Keys.CAP_USER_FRIENDS)
        private int friends;

        public int getNews() {
            return news;
        }

        public int getAlbums() {
            return albums;
        }

        public int getPhotos() {
            return photos;
        }

        public int getSubscribers() {
            return subscribers;
        }

        public int getFriends() {
            return friends;
        }
    }
}
