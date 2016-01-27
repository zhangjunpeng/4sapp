package com.test4s.gdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CP.
 */
public class CP {

    private Long id;
    private String company_name;
    private String identity_cat;
    private String logo;
    private String user_id;
    private String introuduction;
    private String location;
    private String scale;
    private String webSite;
    private String telePhone;
    private String address;

    public CP() {
    }

    public CP(Long id) {
        this.id = id;
    }

    public CP(Long id, String company_name, String identity_cat, String logo, String user_id, String introuduction, String location, String scale, String webSite, String telePhone, String address) {
        this.id = id;
        this.company_name = company_name;
        this.identity_cat = identity_cat;
        this.logo = logo;
        this.user_id = user_id;
        this.introuduction = introuduction;
        this.location = location;
        this.scale = scale;
        this.webSite = webSite;
        this.telePhone = telePhone;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getIdentity_cat() {
        return identity_cat;
    }

    public void setIdentity_cat(String identity_cat) {
        this.identity_cat = identity_cat;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIntrouduction() {
        return introuduction;
    }

    public void setIntrouduction(String introuduction) {
        this.introuduction = introuduction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
