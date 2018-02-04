package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class EditProfileDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public String shopName;
    public String managerName;
    public String tel;
    public String tel2;
    public String tel3;
    public String fax;
    public String mobile;
    public String email;
    public String address;
    public String description;
}
