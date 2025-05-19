plugins {
    id("com.gradleup.shadow") version "8.3.5" // Import shadow API.
    java // Tell gradle this is a java project.
    eclipse // Import eclipse plugin for IDE integration.
    kotlin("jvm") version "2.0.21" // Import kotlin jvm plugin for kotlin/java integration.
    id("io.freefair.lombok") version "8.13.1" // Automatic lombok support.
}

java {
    sourceCompatibility = JavaVersion.VERSION_17 // Declare java version.
}

group = "uk.hotten.staffog" // Declare bundle identifier.
version = "1.0-beta" // Declare plugin version (will be in .jar).
val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion
    )
    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") {
        expand(props)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }
    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("commons-io:commons-io:2.11.0")
    compileOnly("org.apache.commons:commons-pool2:2.11.1")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    compileOnly("mysql:mysql-connector-java:8.0.32")
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    // implementations will be shaded.
    implementation("org.jooq:jooq:3.18.4")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.1.3")    // JDBC
    implementation("io.r2dbc:r2dbc-spi:1.0.0.RELEASE")              // R2DBC SPI
    implementation("org.mariadb:r2dbc-mariadb:1.3.0")               // R2DBC driver
}

// Utility functions
fun revertPluginYmlVersion() {
    val yaml = file("src/main/resources/plugin.yml")
    val lines = yaml.readLines().toMutableList()
    lines[2] = "version: $version"
    yaml.writeText(lines.joinToString("\n"))
}

// Add the name of the current git branch to the build.
fun currentGitBranch(): String = try {
    val proc = ProcessBuilder(
        "git",
        "-C", rootProject.projectDir.absolutePath,
        "rev-parse", "--abbrev-ref", "HEAD"
    )
        .redirectErrorStream(true)
        .start()

    val out = proc.inputStream.bufferedReader().readText().trim()
    proc.waitFor()

    // If Git failed or gave us garbage, fall back.
    val branch = if (proc.exitValue() != 0 || out.startsWith("fatal")) "ogsuite" else out

    // Keep the classifier file-system & URI safe
    branch
        .lowercase()
        .replace(Regex("[^a-z0-9._-]"), "_")
        .ifBlank { "ogsuite" }
} catch (_: Exception) {
    "ogsuite"
}

// Update plugin.yml before compilation
val updatePluginYmlVersion = tasks.register("updatePluginYmlVersion") {
    doLast {
        val yaml = file("src/main/resources/plugin.yml")
        val lines = yaml.readLines().toMutableList()
        lines[2] = "version: $version-${currentGitBranch()}"
        yaml.writeText(lines.joinToString("\n"))
    }
}

// Ensure reproducible builds.
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    archiveClassifier.set(currentGitBranch())
    from("LICENSE") { into("/") }
    dependencies {
        include(dependency("org.jooq:jooq"))
        include(dependency("org.reactivestreams:reactive-streams"))
        include(dependency("org.mariadb.jdbc:mariadb-java-client"))
        include(dependency("io.r2dbc:r2dbc-spi"))
        include(dependency("org.mariadb:r2dbc-mariadb"))
    }
    minimize {
        exclude(dependency("org.mariadb.jdbc:mariadb-java-client"))
        exclude(dependency("io.r2dbc:r2dbc-spi"))
        exclude(dependency("org.mariadb:r2dbc-mariadb"))
    }
    doLast { revertPluginYmlVersion() }
}

tasks.named("compileJava") {
    dependsOn(updatePluginYmlVersion)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier.set("part")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation") // Triggers deprecation warning messages.
    options.encoding = "UTF-8"
    options.isFork = true
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}
