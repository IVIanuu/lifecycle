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

import com.ivianuu.lifecycle.TestLifecycle.CREATE
import com.ivianuu.lifecycle.TestLifecycle.DESTROY
import com.ivianuu.lifecycle.TestLifecycle.HIDE
import com.ivianuu.lifecycle.TestLifecycle.SHOW
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MutableLifecycleTest {

    private val lifecycle = MutableLifecycle<TestLifecycle>()

    @Test
    fun testLastEvent() {
        assertNull(lifecycle.lastEvent)

        lifecycle.onEvent(CREATE)
        assertEquals(CREATE, lifecycle.lastEvent)

        lifecycle.onEvent(SHOW)
        assertEquals(SHOW, lifecycle.lastEvent)
    }

    @Test
    fun testNotifyingListeners() {
        val listener = TestLifecycleListener<TestLifecycle>()
        val expectedHistory = mutableListOf<TestLifecycle>()

        lifecycle.addListener(listener)
        assertEquals(expectedHistory, listener.history)

        lifecycle.onEvent(CREATE)
        expectedHistory.add(CREATE)
        assertEquals(expectedHistory, listener.history)

        lifecycle.onEvent(SHOW)
        expectedHistory.add(SHOW)
        assertEquals(expectedHistory, listener.history)

        lifecycle.onEvent(HIDE)
        expectedHistory.add(HIDE)
        assertEquals(expectedHistory, listener.history)

        lifecycle.onEvent(DESTROY)
        expectedHistory.add(DESTROY)
        assertEquals(expectedHistory, listener.history)
    }

    @Test
    fun testRemoveListeners() {
        val listener = TestLifecycleListener<TestLifecycle>()
        lifecycle.addListener(listener)
        lifecycle.removeListener(listener)

        lifecycle.onEvent(CREATE)
        assertEquals(emptyList<TestLifecycle>(), listener.history)
    }

    @Test
    fun testNotifyingLastEvent() {
        lifecycle.onEvent(CREATE)
        val listener = TestLifecycleListener<TestLifecycle>()
        lifecycle.addListener(listener)
        assertEquals(listOf(CREATE), listener.history)
    }
}