package com.technotapp.servicestation.connection.restapi.sto;


import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class ProductSto extends BaseSto {
    public List<DataModel> dataModel;

    @DontObfuscate
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
