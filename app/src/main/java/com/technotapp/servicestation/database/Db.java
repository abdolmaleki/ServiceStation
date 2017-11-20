package com.technotapp.servicestation.database;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.database.model.MenuModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Db {

    private static Realm realm;

    public static void init() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
    }

    public static class Menu {
        public static boolean insert(List<MenuModel> menuModels) {
            try {
                realm.beginTransaction();
                realm.delete(MenuModel.class);
                realm.commitTransaction();
                realm.beginTransaction();
                realm.insert(menuModels);
                realm.commitTransaction();
                return true;

            } catch (Exception e) {
                AppMonitor.reportBug(e, "Db", "insert");
                return false;
            }
        }

        public static RealmResults<MenuModel> getAll() {
            return realm.where(MenuModel.class).findAll();
        }

        public static RealmResults<MenuModel> getMainMenu() {
            return realm.where(MenuModel.class).equalTo("parentMenuID", -1).findAll();
        }

        public static RealmResults<MenuModel> getSubMenu(int parentId) {
            return realm.where(MenuModel.class).equalTo("parentMenuID", parentId).findAll();
        }

        public static MenuModel getMenuById(long id) {
            return realm.where(MenuModel.class).equalTo("id", id).findFirst();
        }
    }
}
