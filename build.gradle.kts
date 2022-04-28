import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "tech.cordona"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["testcontainersVersion"] = "1.16.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
	implementation("org.springframework.boot:spring-boot-starter-mail:2.6.7")
	implementation("org.junit.jupiter:junit-jupiter:5.8.2")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// JWT
	implementation("com.auth0:java-jwt:3.19.1")
	// Validation
	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.glassfish.web:el-impl:2.2.1-b05")
	// Logging
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
	implementation("ch.qos.logback:logback-classic:1.2.11")
	// Vault
	implementation("org.springframework.cloud:spring-cloud-starter-vault-config:3.1.0")
	implementation("org.springframework.cloud:spring-cloud-vault-config-databases:3.1.0")
	// Mongock
	implementation("com.github.cloudyrock.mongock:mongock-bom:5.0.2.BETA")
	implementation("io.mongock:mongock-springboot:5.0.39")
	implementation("io.mongock:mongodb-springdata-v3-driver:5.0.39")
	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.assertj:assertj-core:3.22.0")
	// Testcontainers
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
