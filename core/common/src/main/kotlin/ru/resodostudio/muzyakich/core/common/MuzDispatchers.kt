package ru.resodostudio.muzyakich.core.common

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Qualifier
annotation class Dispatcher(val muzDispatcher: MuzDispatchers)

enum class MuzDispatchers {
    Default,
    IO,
    Main,
}