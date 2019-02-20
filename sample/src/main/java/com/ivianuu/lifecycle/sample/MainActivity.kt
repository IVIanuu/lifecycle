package com.ivianuu.lifecycle.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.ivianuu.lifecycle.LifecycleOwner
import com.ivianuu.lifecycle.android.lifecycle.doOnResume
import com.ivianuu.lifecycle.android.lifecycle.toLifecycle

class MainActivity : AppCompatActivity(), LifecycleOwner<Lifecycle.Event> {

    override val lifecycle = getLifecycle().toLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.doOnResume { Log.d("testt", "cool resumed") }

        lifecycle.addListener { Log.d("testt", "on event $it") }
    }
}
