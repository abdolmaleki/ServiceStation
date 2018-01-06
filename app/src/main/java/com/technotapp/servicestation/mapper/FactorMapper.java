package com.technotapp.servicestation.mapper;

import android.content.Context;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.connection.restapi.dto.AddFactorDto;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;
import java.util.List;

public class FactorMapper {

    public static AddFactorDto convertModelToDto(Context context, FactorModel model) {
        try {
            Session session = Session.getInstance(context);
            AddFactorDto dto = new AddFactorDto();
            dto.setCustomer("");
            dto.setDiscountPrice(0);
            dto.setTotalPrice(0);
            dto.setFinalPrice(model.getTotalPrice());
            dto.setTerminalCode(session.getTerminalId());
            dto.setTokenId(session.getTokenId());
            List<AddFactorDto.Product> products = new ArrayList<>();
            for (ProductFactorAdapterModel product : model.getProductModels()) {
                AddFactorDto.Product newProduct = new AddFactorDto.Product();
                newProduct.setProductId(product.getNidProduct());
                newProduct.setUnitPrice(Long.parseLong(product.getUnitPrice()));
                newProduct.setTotalPrice(Long.parseLong(product.getSumPrice()));
                newProduct.setValue(Double.parseDouble(String.valueOf(product.getAmount())));
                products.add(newProduct);
            }
            dto.setProducts(products);
            return dto;
        } catch (Exception e) {
            AppMonitor.reportBug(context,e, "FactorMapper", "convertModelToDto");
            return null;
        }

    }
}
