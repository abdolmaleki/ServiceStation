package com.technotapp.servicestation.database.model;

import io.realm.RealmObject;

public class PaymentModel extends RealmObject {
    public String code;
    public String title;
    public String description;
}
