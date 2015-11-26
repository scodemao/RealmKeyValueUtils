package com.github.codemao.model;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by maoweiwei on 15/11/26.
 */
public class RealmKVHelper {

    private static RealmKVHelper instance;
    private static Realm mRealm;

    public RealmKVHelper(Context mContext) {
        RealmConfiguration config = new RealmConfiguration.Builder(mContext)
                .name("key_value_db")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(config);
    }

    // * A static method to initialize a new instance of this
    public synchronized static void initialize(Context mContext) {

        if (instance == null)
            instance = new RealmKVHelper(mContext);
    }

    public static RealmKVHelper getInstance() throws NullPointerException {
        if (instance == null) {
            throw new NullPointerException(
                    "The class has never been initialized. "
                            + "Use initialize(context) first to create a new instance");
        }
        return instance;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public <K, V> boolean addValue(K dataKey, V value) {
        boolean pass = addOrUpdate(String.valueOf(dataKey),
                String.valueOf(value));
        return pass;
    }

    public <K, V> boolean addValuesArray(K dataKey, V[] values) {
        String value = "";
        for (int i = 0; i < (values.length - 1); i++) {
            value += values[i] + ",";
        }
        value += values[(values.length - 1)];

        boolean pass = addOrUpdate(String.valueOf(dataKey),
                String.valueOf(value));
        return pass;
    }

    public <K> Integer getIntValue(K dataKey, Integer defaultValue) {
        Integer value = defaultValue;
        String stringValue = getStringValue(dataKey, null);
        if (stringValue != null) {
            try {
                value = Integer.valueOf(stringValue);
            } catch (Exception e) {
            }
        }
        return value;
    }

    public <K> Long getLongValue(K dataKey, Long defaultValue) {
        Long value = defaultValue;
        String stringValue = getStringValue(dataKey, null);
        if (stringValue != null) {
            try {
                value = Long.valueOf(stringValue);
            } catch (Exception e) {
            }
        }
        return value;
    }

    public <K> Double getDoubleValue(K dataKey, Double defaultValue) {
        Double value = defaultValue;
        String stringValue = getStringValue(dataKey, null);
        if (stringValue != null) {
            try {
                value = Double.valueOf(stringValue);
            } catch (Exception e) {
            }
        }
        return value;
    }

    public <K> Float getFloatValue(K dataKey, Float defaultValue) {
        Float value = defaultValue;
        String stringValue = getStringValue(dataKey, null);
        if (stringValue != null) {
            try {
                value = Float.valueOf(stringValue);
            } catch (Exception e) {
            }
        }
        return value;
    }


    public <K> Boolean getBooleanValue(K dataKey, Boolean defaultValue) {
        Boolean value = defaultValue;
        String stringValue = getStringValue(dataKey, null);
        if (stringValue != null) {
            try {
                value = Boolean.valueOf(stringValue);
            } catch (Exception e) {
            }
        }
        return value;
    }

    public <K> String[] getStringArray(K dataKey) {
        String[] values = null;
        String value = getStringValue(dataKey, null);
        if (value != null) {
            values = value.split(",");
        }

        return values;
    }

    public <K> String getStringValue(K dataKey, String defaultValue) {
        RealmResults<KeyValueRealmObject> datas = mRealm.where(KeyValueRealmObject.class)
                .equalTo("key", String.valueOf(dataKey))
                .findAll();

        if (datas == null || datas.isEmpty())
            return defaultValue;

        return datas.get(0).getValue();
    }

    private boolean addOrUpdate(String dataKey, String value) {
        KeyValueRealmObject realmObject = new KeyValueRealmObject();
        realmObject.setKey(dataKey);
        realmObject.setValue(value);
        saveObject(realmObject);
        return true;
    }


    public <T extends RealmObject> void saveAllObject(List<T> objects) {
        mRealm.beginTransaction();
        for (T object : objects) {
            mRealm.copyToRealm(object);
        }
        mRealm.commitTransaction();
    }

    public <T extends RealmObject> void saveObject(T object) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(object);
        mRealm.commitTransaction();
    }

    public void removeObject(final RealmObject realmObject) {
        if (null == realmObject)
            return;
        mRealm.beginTransaction();
        realmObject.removeFromRealm();
        mRealm.commitTransaction();
    }

    public <T extends RealmObject> void removeObjectList(final RealmList<T> list) {

        if (null == list)
            return;

        mRealm.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).removeFromRealm();
        }
        mRealm.commitTransaction();
    }

    public <T extends RealmObject> RealmResults<T> getAllObjects(final Class<T> clazz) {
        return mRealm.where(clazz).findAll();
    }

    public void removeObjectList(RealmResults<?> result) {
        if (null == result)
            return;
        mRealm.beginTransaction();
        for (int i = 0; i < result.size(); i++) {
            result.get(i).removeFromRealm();
        }
        mRealm.commitTransaction();
    }

}
