package com.example.mosque_management_system.models;

import com.google.gson.annotations.SerializedName;

public class Mosque {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("village")
    private String village;

    @SerializedName("union")
    private String unionName;

    @SerializedName("upazila")
    private String upazila;

    @SerializedName("zilla")
    private String zilla;

    @SerializedName("imamName")
    private String imamName;

    @SerializedName("mosqueCode")
    private String mosqueCode;

    @SerializedName("adminId")
    private String adminId;

    @SerializedName("isMember")
    private boolean isMember;

    // ✅ Constructor for creation
    public Mosque(String name, String address, String village, String unionName, String upazila, String zilla, String imamName) {
        this.name = name;
        this.address = address;
        this.village = village;
        this.unionName = unionName;
        this.upazila = upazila;
        this.zilla = zilla;
        this.imamName = imamName;
    }

    // ✅ Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getVillage() {
        return village;
    }

    public String getUnionName() {
        return unionName;
    }

    public String getUpazila() {
        return upazila;
    }

    public String getZilla() {
        return zilla;
    }

    public String getImamName() {
        return imamName;
    }

    public String getMosqueCode() {
        return mosqueCode;
    }

    public String getAdminId() {
        return adminId;
    }

    public boolean isMember() {
        return isMember;
    }
}
