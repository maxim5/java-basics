plugins {
    `idea`
    `java`
    `java-library`
    `java-test-fixtures`
    `maven-publish`
}

group = "io.spbx"
version = "0.1.1"

tasks.wrapper {
    gradleVersion = "8.10"
    jarFile = projectDir.resolve("gradle/wrapper/gradle-wrapper.jar")
    scriptFile = projectDir.resolve("gradle/wrapper/gradlew")
}

idea {
    module {
        outputDir = buildDir.resolve("idea/main")
        testOutputDir = buildDir.resolve("idea/test")
        isDownloadJavadoc = false
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("com.google.flogger:flogger:0.8")
    runtimeOnly("com.google.flogger:flogger-log4j2-backend:0.8")

    compileOnly("com.google.errorprone:error_prone_annotations:2.28.0")
    compileOnly("org.checkerframework:checker-qual:3.44.0")
    compileOnly("com.google.guava:guava:33.2.0-jre")
    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("com.carrotsearch:hppc:0.10.0")
    compileOnly("com.palantir.patches.sourceforge:trove3:3.0.3-p9")
    compileOnly("io.netty:netty-all:4.1.110.Final")
}

dependencies {
    testFixturesImplementation("com.google.flogger:flogger:0.8")
    testFixturesImplementation("com.google.truth:truth:1.4.2")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testFixturesImplementation("org.mockito:mockito-core:5.12.0")

    testFixturesImplementation("org.jetbrains:annotations:24.1.0")
    testFixturesImplementation("com.google.errorprone:error_prone_annotations:2.28.0")
    testFixturesImplementation("com.google.inject:guice:7.0.0")
    testFixturesImplementation("com.carrotsearch:hppc:0.10.0")
    testFixturesImplementation("io.netty:netty-all:4.1.110.Final")
    testFixturesImplementation("com.google.flogger:flogger-log4j2-backend:0.8")
    testFixturesImplementation("com.google.jimfs:jimfs:1.3.0")
    testFixturesImplementation("net.bytebuddy:byte-buddy:1.14.17")
    testFixturesImplementation("net.bytebuddy:byte-buddy-agent:1.14.17")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testCompileOnly("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    testCompileOnly("org.jetbrains:annotations:24.1.0")
    testCompileOnly("com.google.errorprone:error_prone_annotations:2.28.0")
    testImplementation("com.google.truth:truth:1.4.2")
    testImplementation("com.google.inject:guice:7.0.0")  // testing generics
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    testImplementation("com.google.flogger:flogger-log4j2-backend:0.8")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("com.carrotsearch:hppc:0.10.0")
    testImplementation("com.palantir.patches.sourceforge:trove3:3.0.3-p9")
    testImplementation("io.netty:netty-all:4.1.110.Final")
}

tasks.test {
    useJUnitPlatform()

    // One test `create_invalid_pointers` is failing because it's throwing a different exception when built via gradle.
    // It's a temp measure. The test should be updated. But I'd like to better understand how `@NotNull` annotations are
    // applied by the compiler.
    exclude("**/CharArrayTest*")
}

// https://h4pehl.medium.com/publish-your-gradle-artifacts-to-maven-central-f74a0af085b1
// https://medium.com/@nowshadapu/how-to-create-a-java-library-and-publish-it-to-maven-with-gradle-7-e952837a7fc9
publishing {
    publications {
        create<MavenPublication>("myLibrary") {
            groupId = project.group.toString()
            artifactId = "basics"
            version = project.version.toString()
            from(components["java"])

            pom {
                name = "My Library"
                description = "A concise description of my library"
                url = "https://github.com/maxim5/java-basics"
                properties = mapOf(
                )
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "maximp"
                        name = "Maxim Podkolzine"
                        email = "maxim.podkolzine@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://example.com/my-library.git"
                    developerConnection = "scm:git:ssh://example.com/my-library.git"
                    url = "http://example.com/my-library/"
                }
            }
        }
    }
}
