package cn.netbuffer.apigateway.model;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;
    private String name;
    private String sex;
    private Integer age;
    private String phone;
    private String deliveryaddress;
    private Integer adddate;
    private transient String comments;

    public User() {
    }

    public User(Long id) {
        this.id=id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public Integer getAdddate() {
        return adddate;
    }

    public void setAdddate(Integer adddate) {
        this.adddate = adddate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}