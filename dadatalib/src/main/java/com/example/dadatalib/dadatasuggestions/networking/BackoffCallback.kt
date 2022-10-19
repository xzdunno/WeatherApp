package app.illabo.dadatasuggestions.networking

import android.os.Handler
import android.os.Looper
import retrofit2.Call
import retrofit2.Callback
import kotlin.math.max
import kotlin.math.pow

/**
 * Created by milechainsaw on 7/19/16.
 *
 * Exponential backoff callback for Retrofit2
 * https://gist.github.com/milechainsaw/811c1b583706da60417ed10d35d2808f
 *
 */
abstract class BackoffCallback<T>(
    private val retries: Int = 3,
    private val baseDelay: Double = 300.0
) : Callback<T> {
    private var retryCount = 0
    override fun onFailure(call: Call<T>, t: Throwable) {
        retryCount++
        if (retryCount <= retries) {
            val expDelay =
                (baseDelay * 2.0.pow(max(0, retryCount - 1).toDouble())).toInt()
            Handler(Looper.myLooper() ?: Looper.getMainLooper())
                .postDelayed(Runnable { retry(call) }, expDelay.toLong())
        } else {
            onFailedAfterRetry(call, t)
        }
    }

    private fun retry(call: Call<T>) {
        call.clone().enqueue(this)
    }

    abstract fun onFailedAfterRetry(call: Call<T>, t: Throwable)
}