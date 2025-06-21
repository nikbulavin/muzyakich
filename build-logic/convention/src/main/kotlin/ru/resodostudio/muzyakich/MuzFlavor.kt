package ru.resodostudio.muzyakich

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.plusAssign

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

@Suppress("EnumEntryName")
enum class MuzFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: MuzFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.entries.forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            MuzFlavor.entries.forEach { csFlavor ->
                register(csFlavor.name) {
                    dimension = csFlavor.dimension.name
                    flavorConfigurationBlock(this, csFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (csFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = csFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
