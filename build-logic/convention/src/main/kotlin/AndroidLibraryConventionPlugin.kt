import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import ru.resodostudio.muzyakich.configureFlavors
import ru.resodostudio.muzyakich.configureKotlinAndroid
import ru.resodostudio.muzyakich.libs

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 37
                lint.targetSdk = 37
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                configureFlavors(this)
                testOptions.animationsDisabled = true
                resourcePrefix = path.split("""\W""".toRegex())
                    .drop(1)
                    .distinct()
                    .joinToString(separator = "_")
                    .lowercase() + "_"
            }
            dependencies {
                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())

                "testImplementation"(kotlin("test"))
                "testImplementation"(libs.findLibrary("junit").get())

                "implementation"(libs.findLibrary("androidx.tracing").get())
            }
        }
    }
}
