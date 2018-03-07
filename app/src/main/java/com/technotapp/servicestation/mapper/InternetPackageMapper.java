package com.technotapp.servicestation.mapper;


import com.technotapp.servicestation.adapter.DataModel.InternetPackageModel;
import com.technotapp.servicestation.connection.restapi.sto.InternetPackageSto;

import java.util.ArrayList;
import java.util.List;

public class InternetPackageMapper {
    public static ArrayList<InternetPackageModel> convertStoToAdapterModel(List<InternetPackageSto.InternetPackListResult> stos, int simcardType) {
        try {
            ArrayList<InternetPackageModel> adapterModels = new ArrayList<>();
            for (InternetPackageSto.InternetPackListResult sto : stos) {
                if (sto.profileTypeID == simcardType) {
                    InternetPackageModel walletAdapterModel = new InternetPackageModel();
                    walletAdapterModel.serviceID = sto.serviceID;
                    walletAdapterModel.profileName = sto.profileName;
                    walletAdapterModel.profileTitle = sto.profileTitle;
                    walletAdapterModel.serviceName = sto.serviceName;
                    walletAdapterModel.profileTypeID = sto.profileTypeID;
                    walletAdapterModel.servicePrice = sto.servicePrice;
                    adapterModels.add(walletAdapterModel);
                }
            }

            return adapterModels;
        } catch (Exception e) {
            return null;
        }
    }
}
