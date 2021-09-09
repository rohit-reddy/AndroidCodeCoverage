package rohit.reddy;

import android.app.Application;
import android.util.Log;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: invoked");

        AppManager appManager = new AppManager(getApplicationContext(), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Log.d(TAG, "invoke: Lambda called !");
                return Unit.INSTANCE;
            }
        });

        appManager.syncData(getApplicationContext());

    }
}
