package com.silkpay.techtask.pojo;

import java.util.Objects;

public class JwtResponse {
    private String status;
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;



    public JwtResponse(String status) {
        this.status = status;
    }
    public JwtResponse(String status,String username){
        this.status = status;
        this.username = username;
    }
    public JwtResponse(String status, String accessToken, Long id, String username) {
        this.status = status;
        this.token = accessToken;
        this.id = id;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(token, that.token) &&
                Objects.equals(id, that.id) &&
                Objects.equals(username, that.username);
    }

    // Implement the hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(status, token, id, username);
    }
}
