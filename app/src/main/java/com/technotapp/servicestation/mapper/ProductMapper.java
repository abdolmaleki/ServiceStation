package com.technotapp.servicestation.mapper;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.adapter.DataModel.ProductAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.connection.restapi.dto.AddProductDto;
import com.technotapp.servicestation.database.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductModel convertDtoToModel(AddProductDto dto) {
        try {
            ProductModel model = new ProductModel();
            model.description = dto.description;
            model.title = dto.title;
            model.price = String.valueOf(Math.round(dto.price));
            model.unitCode = dto.unitCode;
            model.nidProduct = (dto.nidProduct != null) ? dto.nidProduct : -1;
            return model;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductMapper", "convertDtoToModel");
            return null;
        }
    }

    private static ProductAdapterModel convertModelToAdapterModel(ProductModel model) {
        try {
            ProductAdapterModel adapterModel = new ProductAdapterModel();
            adapterModel.setDescription(model.description);
            adapterModel.setId(model.nidProduct);
            adapterModel.setName(model.title);
            adapterModel.setPrice(model.price);
            adapterModel.setUnit(String.valueOf(model.unitCode));
            return adapterModel;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductMapper", "convertModelToAdapterModel");
            return null;

        }
    }

    private static ProductFactorAdapterModel convertModelToFactorAdapterModel(ProductModel model) {
        try {
            ProductFactorAdapterModel adapterModel = new ProductFactorAdapterModel();
            adapterModel.setDescription(model.description);
            adapterModel.setnidProduct(model.nidProduct);
            adapterModel.setName(model.title);
            adapterModel.setUnitPrice(model.price);
            adapterModel.setUnit(String.valueOf(model.unitCode));
            return adapterModel;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductMapper", "convertModelToFactorAdapterModel");
            return null;

        }
    }

    public static ArrayList<ProductAdapterModel> convertModelToAdapterModel(List<ProductModel> models) {
        try {
            ArrayList<ProductAdapterModel> adapterModels = new ArrayList<>();
            for (ProductModel model : models) {
                adapterModels.add(convertModelToAdapterModel(model));
            }
            return adapterModels;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductMapper", "convertModelToAdapterModel");
            return null;
        }
    }

    public static ArrayList<ProductFactorAdapterModel> convertModelToFactorAdapterModel(List<ProductModel> models) {
        try {
            ArrayList<ProductFactorAdapterModel> adapterModels = new ArrayList<>();
            for (ProductModel model : models) {
                adapterModels.add(convertModelToFactorAdapterModel(model));
            }
            return adapterModels;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductMapper", "convertModelToFactorAdapterModel");
            return null;
        }
    }
}
