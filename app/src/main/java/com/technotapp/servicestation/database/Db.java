package com.technotapp.servicestation.database;

import android.content.Context;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.database.model.ProductModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Db {

    private static Realm realm;
    private static Context mContext;

    public static void init(Context context) {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
            mContext = context;
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
                AppMonitor.reportBug(mContext, e, "Db", "insert");
                return false;
            }
        }

        public static RealmResults<MenuModel> getAll() {
            return realm.where(MenuModel.class).findAll();
        }

        public static RealmResults

                <MenuModel> getMainMenu() {
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
        public static boolean insert(ProductModel productModel, long nidProduct) {
            try {
                realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
                    @Override
                    public void execute(Realm realm) {
                        productModel.setId(nidProduct);
                        realm.insertOrUpdate(productModel);
                    }
                });
                return true;
            } catch (Exception e) {
                AppMonitor.reportBug(mContext, e, "Product", "insert");
                return false;
            }
        }

        public static boolean update(ProductModel productModel, long nidProduct) {
            try {
                ProductModel toEdit = realm.where(ProductModel.class)
                        .equalTo("nidProduct", nidProduct).findFirst();
                realm.beginTransaction();
                toEdit.title = productModel.title;
                toEdit.price = productModel.price;
                toEdit.unitCode = productModel.unitCode;
                toEdit.description = productModel.description;
                realm.commitTransaction();
                return true;
            } catch (Exception e) {
                AppMonitor.reportBug(mContext, e, "Product", "update");
                return false;
            }
        }

        public static RealmResults<ProductModel> getAll() {
            return realm.where(ProductModel.class).findAll();
        }

        public static ProductModel getProductById(long nidProduct) {
            return realm.where(ProductModel.class).equalTo("nidProduct", nidProduct).findFirst();
        }
    }


    public static class Factor {
        public static long insert(FactorModel factorModel) {
            try {
                realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
                    @Override
                    public void execute(Realm realm) {
                        // increment index
                        Number currentIdNum = realm.where(FactorModel.class).max("id");
                        long nextId;
                        if (currentIdNum == null) {
                            nextId = 1001;
                        } else {
                            nextId = currentIdNum.longValue() + 1;
                        }
                        factorModel.setId(nextId);
                        realm.insertOrUpdate(factorModel);
                    }
                });
                return factorModel.getId();
            } catch (Exception e) {
                AppMonitor.reportBug(mContext, e, "Factor", "insert");
                return -1;
            }
        }

        public static boolean update(FactorModel factorModel, long id) {
            try {
                FactorModel toEdit = realm.where(FactorModel.class)
                        .equalTo("id", id).findFirst();
                realm.beginTransaction();
                toEdit.setPaid(factorModel.isPaid());
                realm.commitTransaction();
                return true;
            } catch (Exception e) {
                AppMonitor.reportBug(mContext, e, "Factor", "update");
                return false;
            }
        }

        public static RealmResults<FactorModel> getAll() {
            return realm.where(FactorModel.class).findAll();
        }

        public static FactorModel getFactorById(long id) {
            return realm.where(FactorModel.class).equalTo("id", id).findFirst();
        }
    }
}
