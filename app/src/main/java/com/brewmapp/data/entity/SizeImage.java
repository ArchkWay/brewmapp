
package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

//	"size": {
////            "0": 1280,
////            "1": 1024,
////            "2": 2,
////            "3": "width=\"1280\" height=\"1024\"",
////            "bits": 8,
////            "channels": 3,
////            "mime": "image\/jpeg"
////            },
public class SizeImage {

    @SerializedName("0")
    private int m0;
    @SerializedName("1")
    private int m1;
    @SerializedName("2")
    private int m2;
    @SerializedName("3")
    private String m3;

    @SerializedName("bits")
    private int mBits;
    @SerializedName("channels")
    private int mChannels;
    @SerializedName("mime")
    private String mMime;

    public int get0() {

        return m0;
    }
    public void set0(int m0) {

        this.m0 = m0;
    }
    public int get1() {

        return m1;
    }
    public void set1(int m1) {

        this.m1 = m1;
    }
    public int get2() {

        return m2;
    }
    public void set2(int m2) {

        this.m2 = m2;
    }
    public String get3() {

        return m3;
    }
    public void set3(String m3) {

        this.m3 = m3;
    }
    public int getmBits() {

        return mBits;
    }
    public void setBits(int mBits) {

        this.mBits = mBits;
    }
    public int getChannels() {

        return mChannels;
    }
    public void setChannels(int mChannels) {

        this.mChannels = mChannels;
    }
    public String getMime() {

        return mMime;
    }
    public void setMime(String mMime) {

        this.mMime = mMime;
    }
}
