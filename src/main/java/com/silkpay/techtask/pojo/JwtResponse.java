package com.silkpay.techtask.pojo;

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

}
