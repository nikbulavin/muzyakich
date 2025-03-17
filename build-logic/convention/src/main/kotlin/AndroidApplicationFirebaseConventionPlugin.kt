import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import ru.resodostudio.muzyakich.MuzFlavor
import ru.resodostudio.muzyakich.libs

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.firebase.crashlytics")
            apply(plugin = "com.google.firebase.firebase-perf")
            apply(plugin = "com.google.gms.google-services")

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
            }

            extensions.configure<ApplicationExtension> {
                productFlavors.configureEach {
                    configure<CrashlyticsExtension> {
                        mappingFileUploadEnabled = name != MuzFlavor.demo.name
                    }
                }
            }
        }
    }
}