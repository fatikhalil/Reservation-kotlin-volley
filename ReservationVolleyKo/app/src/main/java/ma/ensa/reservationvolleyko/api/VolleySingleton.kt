package ma.ensa.reservationvolleyko.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {
    private var requestQueue: RequestQueue? = null

    init {
        requestQueue = getRequestQueue(context)
    }

    companion object {
        @Volatile
        private var instance: VolleySingleton? = null

        fun getInstance(context: Context): VolleySingleton {
            return instance ?: synchronized(this) {
                instance ?: VolleySingleton(context).also { instance = it }
            }
        }
    }

    private fun getRequestQueue(context: Context): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue!!
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue?.add(req)
    }
}