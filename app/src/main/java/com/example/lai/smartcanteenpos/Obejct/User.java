package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 12/10/2017.
 */

public class User {
    private String wid;
    private String name;
    private String password;
    private String contact;
    private String addr;
    private String ssm;

    public User(){

    }

    public User(String wid, String name,String password,String contact,String addr, String ssm ){

        this.setWid(wid);
        this.setName(name);
        this.setPassword(password);
        this.setContact(contact);
        this.setAddr(addr);
        this.setSsm(ssm);


    }


    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSsm() {
        return ssm;
    }

    public void setSsm(String ssm) {
        this.ssm = ssm;
    }

    public String toString(){

        return "User{" + "WalletID='" + wid + '\'' + ",CompanyName='" + name + '\'' +
                ",password='" + password + '\'' + ",contact='" + contact + '\''+ ",address='" + addr
                + '\'' + ",ssm='" + ssm + '}';

    }
}
