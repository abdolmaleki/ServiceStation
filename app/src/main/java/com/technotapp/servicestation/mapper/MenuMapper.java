package com.technotapp.servicestation.mapper;

import com.technotapp.servicestation.connection.restapi.sto.MenuSto;
import com.technotapp.servicestation.database.model.MenuModel;

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


    private static MenuModel convertStoToModel(MenuSto.DataModel.Menu sto) {
        MenuModel model = new MenuModel();
        model.id = sto.menuID;
        model.action = sto.action;
        model.controller = sto.controller;
        model.description = sto.description;
        model.parentMenuID = (sto.parentMenuID != null) ? sto.parentMenuID : -1;
        if (sto.parentMenuID == null) {
            model.orderNo = (sto.orderNo != null) ? sto.orderNo : -1;
        } else {
            model.orderNo = (sto.orderNoSubMenu != null) ? sto.orderNoSubMenu : -1;
        }
        model.icon = sto.icon;
        model.status = sto.status;
        model.url = sto.url;
        model.title = sto.title;
        return model;
    }
}
