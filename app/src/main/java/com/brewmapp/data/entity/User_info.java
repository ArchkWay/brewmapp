package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 28.10.2017.
 */

public class User_info implements Serializable {
    private String id;
    private String phone;
    private String code;
    private String token;
    private String firstname;
    private String lastname;
    private String email;
    private String fb;
    private String fb_access_token;
    private String fb_expires;
    private String image;
    private String gender;
    private String timestamp;
    private String login;
    private String password;
    private String country_id;
    private String role;
    private String city_id;
    private String resto_work_id;
    private String profession_id;
    private String birthday;
    private String family_status;
    private String mob_phone;
    private String addit_phone;
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
    private String last_login;
    private String city_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getFb_access_token() {
        return fb_access_token;
    }

    public void setFb_access_token(String fb_access_token) {
        this.fb_access_token = fb_access_token;
    }

    public String getFb_expires() {
        return fb_expires;
    }

    public void setFb_expires(String fb_expires) {
        this.fb_expires = fb_expires;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getResto_work_id() {
        return resto_work_id;
    }

    public void setResto_work_id(String resto_work_id) {
        this.resto_work_id = resto_work_id;
    }

    public String getProfession_id() {
        return profession_id;
    }

    public void setProfession_id(String profession_id) {
        this.profession_id = profession_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFamily_status() {
        return family_status;
    }

    public void setFamily_status(String family_status) {
        this.family_status = family_status;
    }

    public String getMob_phone() {
        return mob_phone;
    }

    public void setMob_phone(String mob_phone) {
        this.mob_phone = mob_phone;
    }

    public String getAddit_phone() {
        return addit_phone;
    }

    public void setAddit_phone(String addit_phone) {
        this.addit_phone = addit_phone;
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

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getFormattedName() {
        return String.format("%s %s", firstname, lastname);
    }

}
