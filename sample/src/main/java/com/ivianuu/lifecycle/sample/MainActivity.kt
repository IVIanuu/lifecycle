package com.ivianuu.lifecycle.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.lifecycle.android.lifecycle.toLifecycle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lifecycle = lifecycle.toLifecycle()

        lifecycle.addListener {
            Log.d("testt", "on event $it")
        }
    }
}
