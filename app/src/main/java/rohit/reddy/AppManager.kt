package rohit.reddy

import android.content.Context
import android.util.Log

class AppManager(context : Context, private val myLambda: () -> Unit) {

    companion object{
        const val TAG = "AppManager"
    }

    init {
        Log.d(TAG, "AppManager initialized()")
    }

    fun syncData(applicationContext: Context) {
        myLambda()
        Log.d(TAG, "syncData: invoked")
    }
}

interface IListener{
    fun invoke()
}