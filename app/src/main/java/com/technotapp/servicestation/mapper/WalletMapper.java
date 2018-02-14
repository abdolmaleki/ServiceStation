package com.technotapp.servicestation.mapper;

import com.technotapp.servicestation.adapter.DataModel.WalletAdapterModel;
import com.technotapp.servicestation.connection.restapi.sto.CustomerAccountSto;

import java.util.ArrayList;
import java.util.List;

public class WalletMapper {
    public static ArrayList<WalletAdapterModel> convertStoToAdapterModel(List<CustomerAccountSto.DataModel.CustomerAccount> stos) {
        try {
            ArrayList<WalletAdapterModel> adapterModels = new ArrayList<>();
            for (CustomerAccountSto.DataModel.CustomerAccount sto : stos) {

                WalletAdapterModel walletAdapterModel = new WalletAdapterModel();
                walletAdapterModel.title = sto.title;
                walletAdapterModel.accountNumber = sto.accountNumber;
                walletAdapterModel.isActivePin = sto.isActivePin == 1;
                walletAdapterModel.nidAccountType = sto.nidAccountType;
                adapterModels.add(walletAdapterModel);
            }

            return adapterModels;
        } catch (Exception e) {
            return null;
        }
    }
}
