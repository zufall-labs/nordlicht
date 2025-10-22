import de.thetaphi.forbiddenapis.gradle.CheckForbiddenApis

plugins {
    java
    idea
    checkstyle
    alias(libs.plugins.io.quarkus)
    alias(libs.plugins.io.freefair.lombok)
    alias(libs.plugins.de.thetaphi.forbiddenapis)
}

group = "ai.zufall.nordlicht.calc"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_25 // Gradle should use Java 25 features and Syntax when compiling
    toolchain {
        // Gradle checks for a local Java 25 version and uses it if one is found.
        // If there's no local version, the build crashes. The foojay-resolver-convention plugin is needed then.
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor = JvmVendorSpec.ADOPTIUM // Gradle uses Eclipse Temurin (AdoptOpenJDK HotSpot)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(libs.io.quarkus.quarkusBom)) // The BOM for Quarkus.
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-swagger-ui")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = version
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.named<DefaultTask>("checkstyleMain").configure {
    isEnabled = true
}

tasks.named<DefaultTask>("checkstyleTest").configure {
    isEnabled = true
}

tasks.named<CheckForbiddenApis>("forbiddenApisMain").configure {
    bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-system-out", "jdk-reflection")
    signaturesFiles = project.files("config/forbidden-apis.txt")
    isEnabled = true
}

tasks.named<CheckForbiddenApis>("forbiddenApisTest").configure {
    bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-system-out", "jdk-reflection")
    signaturesFiles = project.files("config/forbidden-apis.txt")
    isEnabled = true
}

tasks.named<CheckForbiddenApis>("forbiddenApisQuarkusGeneratedSources").configure {
    dependsOn(tasks.named("quarkusGenerateCode"))
    signaturesFiles = files() // Don't actually check anything
}

tasks.named<CheckForbiddenApis>("forbiddenApisQuarkusTestGeneratedSources").configure {
    dependsOn(tasks.named("quarkusGenerateCodeTests"))
    signaturesFiles = files() // Don't actually check anything
}



idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

checkstyle {
    configFile = project.file("config/checkstyle.xml")
    toolVersion = "11.0.1"
}
