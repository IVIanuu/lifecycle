/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.lifecycle.livedata

import com.ivianuu.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import com.ivianuu.lifecycle.LifecycleListener

/**
 * A live data for lifecycle events
 */
fun <T> Lifecycle<T>.liveData(): LiveData<T> {
    return object : LiveData<T>() {

        private val listener: LifecycleListener<T> = { value = it }

        override fun onActive() {
            super.onActive()
            addListener(listener)
        }

        override fun onInactive() {
            super.onInactive()
            removeListener(listener)
        }

    }
}