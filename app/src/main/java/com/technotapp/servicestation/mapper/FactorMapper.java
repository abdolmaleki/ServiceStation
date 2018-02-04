package com.technotapp.servicestation.mapper;

import android.content.Context;
import android.util.Log;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.adapter.DataModel.ArchiveTransactionAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.SearchFactorAdapterModel;
import com.technotapp.servicestation.connection.restapi.dto.AddFactorDto;
import com.technotapp.servicestation.connection.restapi.sto.SearchFactorSto;
import com.technotapp.servicestation.connection.restapi.sto.SearchTransactionSto;
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
            dto.setTotalPrice(model.getTotalPrice());
            dto.setFinalPrice(model.getTotalPrice() - dto.getDiscountPrice());
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
            AppMonitor.reportBug(context, e, "FactorMapper", "convertModelToDto");
            return null;
        }

    }

    public static ArrayList<SearchFactorAdapterModel> convertStoToAdaptrerModel(List<SearchFactorSto.DataModel.Result> results) {
        try {
            ArrayList<SearchFactorAdapterModel> models = new ArrayList<>();
            for (SearchFactorSto.DataModel.Result result : results) {
                SearchFactorAdapterModel model = new SearchFactorAdapterModel();
                model.date = DateHelper.miladiToShamsiِDate(result.dateTime);
                model.time = DateHelper.miladiToShamsiِTime(result.dateTime);
                model.discountPrice = result.discountPrice;
                model.factorId = result.nidFactor;
                model.finalPrice = result.finalPrice;
                model.statusTitle = result.statusTitle;
                model.terminalCode = result.terminalCode;
                model.totalPrice = result.totalPrice;


                models.add(model);
            }

            return models;
        } catch (Exception e) {
            Log.e("Erroooooor-->", e.getMessage());
            return null;
        }

    }

}
