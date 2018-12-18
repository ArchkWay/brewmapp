package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.LocalizedStringsDeserializer;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.SimpleImageSource;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ovcst on 24.08.2017.
 */

public class Resto implements ICommonItem, Serializable {

    private int id;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName(Keys.NAME)
    private LocalizedStrings mName;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName(Keys.TEXT)
    private LocalizedStrings mText;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName(Keys.SHORT_TEXT)
    private LocalizedStrings mShortText;

    private String site;
    private String music;
    private String user_id;
    private String advertising;
    private String gpa;
    private String in_archive;
    private String additional_data;
    private String round_clock;
    private Location location;
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
    private String job;
    private String interests;
    private String films;
    private String books;
    private String games;
    private String about;
    private String status;
    private String last_login;
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private String country;
    private Distance distance;

    public Resto() {

    }


    public Resto(JsonElement jsonElement) {

    }

    public Resto(String id, String name) {
        setId(Integer.valueOf(id));
        setName(name);
    }


    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
//        this.name = name;
    }

    public void setText(String text) {
//        this.text = text;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getIn_archive() {
        return in_archive;
    }

    public void setIn_archive(String in_archive) {
        this.in_archive = in_archive;
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getRound_clock() {
        return round_clock;
    }

    public void setRound_clock(String round_clock) {
        this.round_clock = round_clock;
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

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setAvgCost(int avgCost) {
        this.avgCost = avgCost;
    }

    public void setAvgCostMax(int avgCostMax) {
        this.avgCostMax = avgCostMax;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public ImageSource getImageSource() {
        return imageSource;
    }

    public void setImageSource(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    @SerializedName(Keys.GET_THUMB)
    private String thumb;

    @SerializedName(Keys.AVG_COST)
    private int avgCost;

    @SerializedName(Keys.AVG_COST_MAX)
    private int avgCostMax;


    @SerializedName(Keys.PHOTO_COUNT)
    private int photoCount;

    @SerializedName(Keys.LOCATION_ID)
    private int locationId;

    private ImageSource imageSource;

    public int getId() {
        return id;
    }

    public String getName() {
        return mName != null ? mName.toString() : null;
    }

    public String getText() {
        return mText != null ? mText.toString() : null;
    }

    public String getShortText() {
        return mShortText != null ? mShortText.toString() : null;
    }

    public String getSite() {
        return site;
    }

    public String getMusic() {
        return music;
    }

    public String getThumb() {
        if(thumb != null && !thumb.startsWith("http")) {
            thumb = BuildConfig.SERVER_ROOT_URL + thumb;
        }
        return thumb;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getAvgCost() {
        return avgCost;
    }

    public int getAvgCostMax() {
        return avgCostMax;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    @Override
    public String title() {
        return getName();
    }

    @Override
    public int id() {
        return getId();
    }

    @Override
    public ImageSource image() {
        if(imageSource == null && getThumb() != null) {
            imageSource = new SimpleImageSource(getThumb());
        }
        return imageSource;
    }

    public String getAdressFormat() {

        return new StringBuilder()
                .append(getLocation()==null?"":new StringBuilder()

                        .append(getLocation().getCity_id())
                        .append(",")
                        .append(getLocation().getLocation().getStreet())
                        .append(",")
                        .append(getLocation().getLocation().getHouse())
                        )
                .toString()
                ;
    }
}
