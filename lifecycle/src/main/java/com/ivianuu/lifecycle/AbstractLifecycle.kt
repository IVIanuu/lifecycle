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

package com.ivianuu.lifecycle

import com.ivianuu.closeable.Closeable
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Basic lifecycle which handles listeners and the last event
 */
abstract class AbstractLifecycle<T> : Lifecycle<T> {

    override val lastEvent: T?
        get() = _lastEvent
    private var _lastEvent: T? = null

    private val listeners = mutableSetOf<LifecycleListener<T>>()

    private val lock = ReentrantLock()

    override fun addListener(listener: LifecycleListener<T>): Closeable = lock.withLock {
        listeners.add(listener)

        // dispatch the latest event if available
        _lastEvent?.let { listener(it) }

        return@withLock Closeable { removeListener(listener) }
    }

    override fun removeListener(listener: LifecycleListener<T>): Unit = lock.withLock {
        listeners.remove(listener)
    }

    /**
     * Dispatches the [event] to all listeners
     */
    protected open fun onEvent(event: T): Unit = lock.withLock {
        if (_lastEvent != event) {
            _lastEvent = event
            listeners.toList().forEach { it(event) }
        }
    }

}