plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// DB
	runtimeOnly("com.mysql:mysql-connector-j")

	// Jackson - JavaTime (LocalDateTime용)
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Redis Cache
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// Redission
	implementation("org.redisson:redisson-spring-boot-starter:3.25.2")

	// AOP 기능을 위한 의존성 추가
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// Kafka
	implementation("org.springframework.kafka:spring-kafka")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}
