package com.example.ankush.activity1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int id;

    private String email;

    private String password;

    private int IsAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int GetIsAdmin() {
        return IsAdmin;
    }

    public void setAdmin(int admin) {
        IsAdmin = admin;
    }

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        IsAdmin = in.readInt();
    }

    public User(int id, String email, String password, int admin)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.IsAdmin = admin;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(IsAdmin);
    }
}
