package wxm.com.firstaid.module;

import android.net.Uri;

/**
 * Created by Zero on 12/9/2016.
 */

public class User {
    String id;
    String phone;
    String password;
    Uri url=null;

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", is_rescuer='" + is_rescuer + '\'' +
                ", record='" + record + '\'' +
                '}';
    }

    String name;
    String image;
    String sex;
    String age;
    String is_rescuer;
    String record;

    public User(String is_rescuer, String id, String phone, String password, String name, String image, String sex, String age) {
        this.is_rescuer = is_rescuer;
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.image = image;
        this.sex = sex;
        this.age = age;
    }

    public User() {
    }

    public User(String id, String phone, String password, String name) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.name = name;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIs_rescuer() {
        return is_rescuer;
    }

    public void setIs_rescuer(String is_rescuer) {
        this.is_rescuer = is_rescuer;
    }
}
