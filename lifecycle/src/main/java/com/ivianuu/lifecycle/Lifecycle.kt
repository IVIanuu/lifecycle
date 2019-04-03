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

typealias LifecycleListener<T> = (T) -> Unit

/**
 * Lifecycle
 */
interface Lifecycle<T> {

    /**
     * The last event that occurred in this lifecycle if any
     */
    val lastEvent: T?

    /**
     * Notifies the [listener] on any lifecycle event
     */
    fun addListener(listener: LifecycleListener<T>): Closeable

    /**
     * Removes the previously added [listener]
     */
    fun removeListener(listener: LifecycleListener<T>)

}

/**
 * Invokes [block] when the [event] occurs
 */
fun <T> Lifecycle<T>.doOnEvent(event: T, block: () -> Unit): Closeable {
    val listener: LifecycleListener<T> = {
        if (it == event) block()
    }

    return addListener(listener)
}

/**
 * Invokes [block] the first time the [event] occurs
 */
fun <T> Lifecycle<T>.doOnNextEvent(event: T, block: () -> Unit): Closeable {
    val listener = object : LifecycleListener<T> {
        override fun invoke(e: T) {
            if (e == event) {
                block()
                removeListener(this)
            }
        }
    }

    return addListener(listener)
}