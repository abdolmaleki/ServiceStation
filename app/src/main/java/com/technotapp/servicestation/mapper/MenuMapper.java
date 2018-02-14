package com.technotapp.servicestation.mapper;

import com.technotapp.servicestation.connection.restapi.sto.MenuSto;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.database.model.PaymentModel;

import java.util.ArrayList;
import java.util.List;

public class MenuMapper {

    public static List<MenuModel> convertStosToModels(List<MenuSto> stos) {
        List<MenuModel> models = new ArrayList<>();
        for (MenuSto sto : stos) {
            for (MenuSto.DataModel dataModel : sto.dataModel) {
                for (MenuSto.DataModel.Menu menu : dataModel.menu) {
                    models.add(convertStoToModel(menu));
                }
            }
        }
        return models;
    }

    public static List<PaymentModel> convertPaymentStoToModels(List<MenuSto> stos) {
        List<PaymentModel> models = new ArrayList<>();
        for (MenuSto sto : stos) {
            for (MenuSto.DataModel dataModel : sto.dataModel) {
                for (MenuSto.DataModel.PaymentMethod paymentMethod : dataModel.paymentMethods) {
                    models.add(convertPaymentStoToModel(paymentMethod));
                }
            }
        }
        return models;
    }


    private static MenuModel convertStoToModel(MenuSto.DataModel.Menu sto) {
        MenuModel model = new MenuModel();
        model.id = sto.menuID;
        model.action = sto.action;
        model.controller = sto.controller;
        model.description = sto.description;
        model.parentMenuID = (sto.parentMenuID != null) ? sto.parentMenuID : -1;
        model.icon = sto.icon;
        model.url = sto.url;
        model.title = sto.title;
        return model;
    }

    private static PaymentModel convertPaymentStoToModel(MenuSto.DataModel.PaymentMethod sto) {
        PaymentModel model = new PaymentModel();
        model.title = sto.title;
        model.code = sto.code;
        model.description = sto.description;
        return model;
    }
}
