package com.view.s4server;

/**
 * Created by Administrator on 2016/3/3.
 */
public class IssueSimpleInfo {

    private String user_id;
    private String identity_cat;
    private String company_name;
    private String logo;
    private String company_intro;
    private String business_cat;
    private String coop_cat;

    public String getBusiness_cat() {
        return business_cat;
    }

    public void setBusiness_cat(String business_cat) {
        this.business_cat = business_cat;
    }

    public String getCoop_cat() {
        return coop_cat;
    }

    public void setCoop_cat(String coop_cat) {
        this.coop_cat = coop_cat;
    }

    public String getCompany_intro() {
        return company_intro;
    }

    public void setCompany_intro(String company_intro) {
        this.company_intro = company_intro;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIdentity_cat() {
        return identity_cat;
    }

    public void setIdentity_cat(String identity_cat) {
        this.identity_cat = identity_cat;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
