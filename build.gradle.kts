/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
plugins {
  alias(libs.plugins.spotless)
  id("org.sonarsource.php.artifactory-configuration")
  id("org.sonarsource.php.rule-api")
  id("org.sonarsource.php.sonarqube")
}

val kotlinGradleDelimiter = "(package|import|plugins|pluginManagement|dependencyResolutionManagement|repositories) "
//spotless {
//  // Mainly used to define spotless configuration for the build-logic
//  encoding(Charsets.UTF_8)
//  kotlinGradle {
//    ktlint().setEditorConfigPath("$rootDir/.editorconfig")
//    target("*.gradle.kts", "build-logic/*.gradle.kts", "/build-logic/src/**/*.gradle.kts")
//    licenseHeaderFile(
//      rootProject.file("LICENSE_HEADER"),
//      kotlinGradleDelimiter,
//    ).updateYearWithLatest(true)
//  }
//  kotlin {
//    ktlint().setEditorConfigPath("$rootDir/.editorconfig")
//    target("/build-logic/src/**/*.kt")
//    licenseHeaderFile(rootProject.file("LICENSE_HEADER")).updateYearWithLatest(true)
//  }
//}

artifactory {
  val artifactsToPublish = "org.sonarsource.php:sonar-php-plugin:jar"

  clientConfig.info.addEnvironmentProperty("ARTIFACTS_TO_PUBLISH", artifactsToPublish)
  clientConfig.info.addEnvironmentProperty("ARTIFACTS_TO_DOWNLOAD", "")

  setContextUrl(System.getenv("ARTIFACTORY_URL")?:"https://artifactory.autorabit.com/artifactory/")
  publish {
    repository {
      setRepoKey(System.getenv("ARTIFACTORY_CODESCAN_REPO")?: "libs-release-local")
      setUsername(System.getenv("ARTIFACTORY_USER"))
      setPassword(System.getenv("ARTIFACTORY_PWD"))
    }
    defaults {
      publications("mavenJava")
      setProperties(
        mapOf(
          "build.name" to "sonar-php",
          "version" to project.version.toString(),
          "build.number" to project.ext["buildNumber"].toString(),
          "pr.branch.target" to System.getenv("PULL_REQUEST_BRANCH_TARGET"),
          "pr.number" to System.getenv("PULL_REQUEST_NUMBER"),
          "vcs.branch" to System.getenv("GIT_BRANCH"),
          "vcs.revision" to System.getenv("GIT_COMMIT"),
        ),
      )
      setPublishArtifacts(true)
      setPublishPom(true)
      setPublishIvy(false)
    }
  }

  clientConfig.info.addEnvironmentProperty("PROJECT_VERSION", project.version.toString())
  clientConfig.info.buildName = "sonar-php"
  clientConfig.info.buildNumber = project.ext["buildNumber"].toString()
  clientConfig.isIncludeEnvVars = true
  clientConfig.envVarsExcludePatterns =
    "*password*,*PASSWORD*,*secret*,*MAVEN_CMD_LINE_ARGS*,sun.java.command," +
      "*token*,*TOKEN*,*LOGIN*,*login*,*key*,*KEY*,*PASSPHRASE*,*signing*"
}

tasks.artifactoryPublish { skip = true }
