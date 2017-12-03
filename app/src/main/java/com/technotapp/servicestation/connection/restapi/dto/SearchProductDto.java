package com.technotapp.servicestation.connection.restapi.dto;

public class SearchProductDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public Long nidProduct;
    public Byte unitCode;
    public String title;
    public String description;
    public Integer skipRows;
    public Integer takeRows;

}
