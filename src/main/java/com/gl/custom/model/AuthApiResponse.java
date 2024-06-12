package com.gl.custom.model;

public class AuthApiResponse {
    String token_type,expires_in,access_token;

    public String getToken_type() {return token_type;}

    public void setToken_type(String token_type) {this.token_type = token_type;}

    public String getExpires_in() {return expires_in;}

    public void setExpires_in(String expires_in) {this.expires_in = expires_in;}

    public String getAccess_token() {return access_token;}

    public void setAccess_token(String access_token) {this.access_token = access_token;}
    public AuthApiResponse(){}
    public AuthApiResponse(String token_type, String expires_in, String access_token) {
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "AuthApiResponse{" +
                "token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
