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

package com.ivianuu.lifecycle.common

import com.ivianuu.lifecycle.Lifecycle
import com.ivianuu.lifecycle.LifecycleOwner
import java.io.Serializable

private object UNINITIALIZED_VALUE

private class LifecycleLazy<E, T>(
    lifecycle: Lifecycle<E>,
    private val event: E,
    initializer: () -> T
) : Lazy<T>, Serializable {

    private var initializer: (() -> T)? = initializer
    @Volatile private var _value: Any? = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = this

    init {
        lifecycle.addListener(object : (E) -> Unit {
            override fun invoke(event: E) {
                if (this@LifecycleLazy.event == event) {
                    if (!isInitialized()) value
                    lifecycle.removeListener(this)
                }
            }
        }
        )
    }

    @Suppress("LocalVariableName")
    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String =
        if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}

/**
 * Returns a [Lazy] which auto initializes on [event]
 */
fun <E, T> Lifecycle<E>.lifecycleLazy(
    event: E,
    initializer: () -> T
): Lazy<T> = LifecycleLazy(this, event, initializer)

/**
 * Returns a [Lazy] which auto initializes on [event]
 */
fun <E, T> LifecycleOwner<E>.lifecycleLazy(
    event: E,
    initializer: () -> T
) = lifecycle.lifecycleLazy(event, initializer)