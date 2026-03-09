/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

fun interface Disposable {
    fun dispose()
}

interface DisposableRegistry {
    fun disposeOnClose(disposable: Disposable)
}

internal class DisposableRegistryImpl : DisposableRegistry, Disposable {
    private val disposables = mutableListOf<Disposable>()

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}