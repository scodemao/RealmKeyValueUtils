# RealmKeyValueUtils

public class AppAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmKVHelper.initialize(this);
    }
}
