package com.qingqingmr.qqmr.web;


/**
 * @author crn
 * @datetime 2018-07-20 15:36:14
 */
public class User {
    private User() {

    }

    public static User getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    private String name;
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public enum Singleton {
        INSTANCE;

        private  User user = null;

        Singleton() {
            user = new User();
        }

        public User getInstance() {
            return user;
        }
    }
}
