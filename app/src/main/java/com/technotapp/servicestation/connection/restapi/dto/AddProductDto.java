package com.technotapp.servicestation.connection.restapi.dto;

public class AddProductDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public Long nidProduct; // only for update
    public Byte unitCode;
    public String title;
    public Double price;
    public String description;

}
