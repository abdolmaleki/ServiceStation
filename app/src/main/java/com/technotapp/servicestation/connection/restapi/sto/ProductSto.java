package com.technotapp.servicestation.connection.restapi.sto;


import java.io.Serializable;
import java.util.List;


public class ProductSto extends BaseSto {
    public List<DataModel> dataModel;

    public static class DataModel implements Serializable {
        public long nidProduct;

        public long getNidProduct() {
            return nidProduct;
        }

        public void setNidProduct(long nidProduct) {
            this.nidProduct = nidProduct;
        }
    }
}
