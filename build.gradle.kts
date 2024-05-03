import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")
}

allprojects {
    group = "${property("projectGroup")}"
    version = "${property("projectVersion")}"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }


    java {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}