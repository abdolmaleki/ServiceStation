
package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class AddFactorDto extends BaseDto {

    private String tokenId;
    private String terminalCode;
    private long totalPrice; // mablaghe kol bedoone takhfif
    private long discountPrice; // takhfif
    private long finalPrice;// mablaghe kol ba takhfif
    private String customer;
    private List<Product> products = null;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public long getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(long finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @DontObfuscate
    public static class Product implements Serializable {

        private long productId;
        private Double value;
        private long unitPrice;
        private long totalPrice;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public long getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(long unitPrice) {
            this.unitPrice = unitPrice;
        }

        public long getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(long totalPrice) {
            this.totalPrice = totalPrice;
        }

    }

}
