package com.technotapp.servicestation.entity;

import java.io.Serializable;

public class SocketService implements Serializable {
    public int idService = 12;
    public Info info;

    public class Info implements Serializable {
        String id1 = "123";
        String id2 = "456";
    }
}
