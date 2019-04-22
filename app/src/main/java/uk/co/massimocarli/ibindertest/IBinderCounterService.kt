package uk.co.massimocarli.ibindertest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class IBinderCounterService : Service(), Counter {

  companion object {
    const val TAG = "IBinderCounterService"
  }

  lateinit var binderCounter: IBinderCounter
  @Volatile
  var running = false

  override fun onCreate() {
    super.onCreate()
    binderCounter = IBinderCounter()
    log("onCreate")
  }

  override fun onBind(intent: Intent?): IBinder? = binderCounter

  override fun startCounter() {
    running = true
    thread {
      for (i in 0..100) {
        if (!running) {
          break
        }
        Thread.sleep(1000)
        if (!running) {
          break
        }
        log("Count: $i")
      }
      log("Completed!")
    }
  }

  override fun stopCounter() {
    running = false;
  }

  inner class IBinderCounter : Binder() {

    fun getCounter(): Counter = this@IBinderCounterService
  }

  override fun onDestroy() {
    super.onDestroy()
    running = false
    log("onDestroy")
  }

  private fun log(msg: String) {
    Log.d(TAG, "-> $msg")
  }
}