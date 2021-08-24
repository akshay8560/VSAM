
package cryptos.cryptocurrency.vsam.models;

public class User {
    private String name, username, dob,createPassword,conformPassword,imageurl,bio,userid ;

    public User() {
    }

    public User(String name, String username, String dob, String createPassword, String conformPassword, String imageurl, String bio, String userid) {
        this.name = name;
        this.username = username;
        this.dob = dob;
        this.createPassword = createPassword;
        this.conformPassword = conformPassword;
        this.imageurl = imageurl;
        this.bio = bio;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreatePassword() {
        return createPassword;
    }

    public void setCreatePassword(String createPassword) {
        this.createPassword = createPassword;
    }

    public String getConformPassword() {
        return conformPassword;
    }

    public void setConformPassword(String conformPassword) {
        this.conformPassword = conformPassword;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
