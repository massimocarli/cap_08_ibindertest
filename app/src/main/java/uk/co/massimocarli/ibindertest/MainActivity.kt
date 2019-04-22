package uk.co.massimocarli.ibindertest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  private lateinit var mCounter: Counter
  private var bounded = false

  private val connection = object : ServiceConnection {

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
      val binder = service as IBinderCounterService.IBinderCounter
      mCounter = binder.getCounter()
      bounded = true
    }

    override fun onServiceDisconnected(arg0: ComponentName) {
      bounded = false
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onStart() {
    super.onStart()
    Intent(this, IBinderCounterService::class.java).also { intent ->
      bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    unbindService(connection)
  }

  fun startCounter(view: View) {
    if (bounded) {
      mCounter.startCounter()
    }
  }

  fun stopCounter(view: View) {
    if (bounded) {
      mCounter.stopCounter()
    }
  }
}
