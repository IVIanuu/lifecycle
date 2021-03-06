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

package com.ivianuu.lifecycle.android.lifecycle
import androidx.lifecycle.GenericLifecycleObserver
import com.ivianuu.lifecycle.AbstractLifecycle
import com.ivianuu.lifecycle.Lifecycle

typealias AndroidxLifecycle = androidx.lifecycle.Lifecycle
typealias AndroidxLifecycleEvent = androidx.lifecycle.Lifecycle.Event

/**
 * A [Lifecycle] which uses a [androidx.lifecycle.Lifecycle] internally
 */
class AndroidLifecycle(
    lifecycle: AndroidxLifecycle
) : AbstractLifecycle<AndroidxLifecycleEvent>() {

    init {
        lifecycle.addObserver(GenericLifecycleObserver { _, event -> onEvent(event) })
    }

}

/**
 * Returns a [Lifecycle] for this android lifecycle
 */
fun AndroidxLifecycle.toLifecycle(): Lifecycle<AndroidxLifecycleEvent> =
    AndroidLifecycle(this)