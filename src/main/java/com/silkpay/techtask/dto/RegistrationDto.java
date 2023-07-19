package com.silkpay.techtask.dto;

public class RegistrationDto {
    private String username;
    private String password;
    public RegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegistrationDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
