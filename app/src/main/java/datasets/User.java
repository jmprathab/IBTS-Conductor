package datasets;

import com.google.gson.Gson;

public class User {
    private int userId;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String password;

    public User() {
    }

    public User(int userId, String mobile, String password) {
        this.userId = userId;
        this.mobile = mobile;
        this.password = password;
    }

    private User(int userId, String name, String mobile, String email, String address, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
    }

    public static String getUserJson(User user) {
        return new Gson().toJson(user);
    }

    public static User getUserObject(String json) {
        return new Gson().fromJson(json, User.class);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        if (this.name == null || this.name.equals("-")) {
            return "-";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return userId == user.userId;

    }

    @Override
    public int hashCode() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
