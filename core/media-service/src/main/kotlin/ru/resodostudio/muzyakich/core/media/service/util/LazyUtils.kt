package ru.resodostudio.muzyakich.core.media.service.util

fun <T> unsafeLazy(initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = initializer)