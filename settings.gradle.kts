import java.util.Properties

pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        java
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}

rootProject.name = "gradle"

val gradleProperties = Properties()

file("gradle.properties").inputStream().use {
    gradleProperties.load(it)
}

val packages = gradleProperties.getProperty("packages").replace(".", "/")

listOf(
    "module-core",
    "module-api",
    "module-infrastructure:database",
    "module-infrastructure:external-api"
).forEach { moduleName ->
    include(moduleName)
    val moduleDirectory = moduleName.replace(":", "/")
    val module = initializeModule(moduleDirectory)
    project(":$moduleName").projectDir = module
}

private fun initializeModule(moduleName: String): File {
    return file(moduleName).let { module ->
        module.mkdirs()
        createBuildScript(module)
        file("$module/src/main/java/$packages").mkdirs()
        file("$module/src/main/resources/").mkdirs()
        file("$module/src/test/java/$packages").mkdirs()
        file("$module/src/test/resources/").mkdirs()
        module
    }
}

private fun createBuildScript(module: File) {
    module.resolve("build.gradle.kts").let {
        if (it.exists()) {
            return
        }

        it.writeText(
            """
            dependencies {}
            """.trimIndent(),
        )
    }
}
