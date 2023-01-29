package com.example.newsapp.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="users")
public class UserEntity{
    @PrimaryKey(autoGenerate = true)
    Integer id;
    @ColumnInfo(name="userId")
    String userId;
    @ColumnInfo(name="password")
    String password;
    @ColumnInfo(name="password_confirm")
    String password_confirm;
    @ColumnInfo(name="name")
    String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword_confirm() {
        return password_confirm;
    }
    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

