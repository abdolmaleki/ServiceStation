package com.technotapp.servicestation.database;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.database.model.ProductModel;

import java.util.List;

import javax.annotation.Nullable;

import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;

public class Db {

    private static Realm realm;

    public static void init() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
    }

    public static class Menu {
        public static boolean insert(final List<MenuModel> menuModels) {
            final RealmResults<MenuModel> results = realm.where(MenuModel.class).findAll();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                }
            });


            try {

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

    public static class Product {
        public static boolean insert(ProductModel productModel) {
            try {
                realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
                    @Override
                    public void execute(Realm realm) {
                        // increment index
                        Number currentIdNum = realm.where(ProductModel.class).max("id");
                        int nextId;
                        if (currentIdNum == null) {
                            nextId = 1;
                        } else {
                            nextId = currentIdNum.intValue() + 1;
                        }
                        productModel.setId(nextId);
                        realm.insertOrUpdate(productModel);
                    }
                });
                return true;
            } catch (Exception e) {
                AppMonitor.reportBug(e, "Product", "insert");
                return false;
            }
        }

        public static boolean update(ProductModel productModel, int id) {
            try {
                ProductModel toEdit = realm.where(ProductModel.class)
                        .equalTo("id",id).findFirst();
                realm.beginTransaction();
                toEdit.title = productModel.title;
                toEdit.price = productModel.price;
                toEdit.unitCode = productModel.unitCode;
                toEdit.description = productModel.description;
                realm.commitTransaction();
                return true;
            } catch (Exception e) {
                AppMonitor.reportBug(e, "Product", "update");
                return false;
            }
        }

        public static RealmResults<ProductModel> getAll() {
            return realm.where(ProductModel.class).findAll();
        }

        public static ProductModel getProductById(int id) {
            return realm.where(ProductModel.class).equalTo("id", id).findFirst();
        }
    }
}
