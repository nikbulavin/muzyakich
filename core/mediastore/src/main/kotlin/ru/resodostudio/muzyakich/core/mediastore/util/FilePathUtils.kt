package ru.resodostudio.muzyakich.core.mediastore.util

import java.io.File

internal fun String.asFolder() = File(this).parentFile?.name.orEmpty()
