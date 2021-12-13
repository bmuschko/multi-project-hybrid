# Multi-project Gradle project

* Demonstrate that subprojects can be compiled and published in the correct order depending of project dependencies.
* Execution of subprojects in parallel using the `org.gradle.parallel=true` option.
* No build cache enabled.

## Project setup

* Low-level projects
    * `low-1`: Declares external dependency on `org.apache.commons:commons-configuration2:2.7`.
    * `low-2`: Declares external dependency on `org.apache.commons:commons-lang3:3.12.0`.
* Mid-level projects
    * `mid-1`: Declares project dependency on `low-1` and `low-2`.
    * `mid-2`: Declares project dependency on `mid-1`.
* Top-level projects
    * `top-1`: Declares project dependency on `mid-1`.

## Use cases

### A developer can compile the code

A developer implements a feature of bugfix. One or many modules can be compiled and tested together in the correct order independent of low/mid/top level without having to publish artifacts.

```
$ ./gradlew compileJava
> Task :low-2:compileJava
> Task :low-1:compileJava
> Task :mid-1:compileJava
> Task :mid-2:compileJava
> Task :top-1:compileJava

$ ./gradlew :mid1:compileJava
> Task :low-2:compileJava
> Task :low-1:compileJava
> Task :mid-1:compileJava
```

### A developer can publish to Maven local

A developer wants to build one or many modules on a specific level of the hierarchy and publish its artifacts to the Maven local directory. Published artifacts contain the correct metadata and can be consumed by other projects via GAV.

```
$ ./gradlew clean compileJava publishToMavenLocal
> Task :top-1:processResources NO-SOURCE
> Task :mid-2:processResources NO-SOURCE
> Task :mid-1:processResources NO-SOURCE
> Task :mid-1:generatePomFileForMavenPublication
> Task :mid-2:generatePomFileForMavenPublication
> Task :top-1:generatePomFileForMavenPublication
> Task :low-2:compileJava
> Task :low-2:processResources NO-SOURCE
> Task :low-2:classes
> Task :low-1:compileJava
> Task :low-2:jar
> Task :low-1:processResources NO-SOURCE
> Task :low-1:classes
> Task :low-1:jar
> Task :low-2:generateMetadataFileForMavenPublication
> Task :low-1:generateMetadataFileForMavenPublication
> Task :low-1:generatePomFileForMavenPublication
> Task :low-2:generatePomFileForMavenPublication
> Task :mid-1:compileJava
> Task :mid-1:classes
> Task :low-1:publishMavenPublicationToMavenLocal
> Task :low-2:publishMavenPublicationToMavenLocal
> Task :low-1:publishToMavenLocal
> Task :low-2:publishToMavenLocal
> Task :mid-1:jar
> Task :mid-1:generateMetadataFileForMavenPublication
> Task :mid-1:publishMavenPublicationToMavenLocal
> Task :mid-1:publishToMavenLocal
> Task :mid-2:compileJava
> Task :mid-2:classes
> Task :top-1:compileJava
> Task :top-1:classes
> Task :mid-2:jar
> Task :top-1:jar
> Task :mid-2:generateMetadataFileForMavenPublication
> Task :top-1:generateMetadataFileForMavenPublication
> Task :mid-2:publishMavenPublicationToMavenLocal
> Task :mid-2:publishToMavenLocal
> Task :top-1:publishMavenPublicationToMavenLocal
> Task :top-1:publishToMavenLocal

$ tree ~/.m2/repository/com/bmuschko
/Users/bmuschko/.m2/repository/com/bmuschko
├── low-1
│   ├── 1.0.0
│   │   ├── low-1-1.0.0.jar
│   │   ├── low-1-1.0.0.module
│   │   └── low-1-1.0.0.pom
│   └── maven-metadata-local.xml
├── low-2
│   ├── 1.0.0
│   │   ├── low-2-1.0.0.jar
│   │   ├── low-2-1.0.0.module
│   │   └── low-2-1.0.0.pom
│   └── maven-metadata-local.xml
├── mid-1
│   ├── 1.0.0
│   │   ├── mid-1-1.0.0.jar
│   │   ├── mid-1-1.0.0.module
│   │   └── mid-1-1.0.0.pom
│   └── maven-metadata-local.xml
├── mid-2
│   ├── 1.0.0
│   │   ├── mid-2-1.0.0.jar
│   │   ├── mid-2-1.0.0.module
│   │   └── mid-2-1.0.0.pom
│   └── maven-metadata-local.xml
└── top-1
    ├── 1.0.0
    │   ├── top-1-1.0.0.jar
    │   ├── top-1-1.0.0.module
    │   └── top-1-1.0.0.pom
    └── maven-metadata-local.xml

$ cat ~/.m2/repository/com/bmuschko/mid-1/1.0.0/mid-1-1.0.0.pom
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- This module was also published with a richer model, Gradle metadata,  -->
  <!-- which should be used instead. Do not delete the following line which  -->
  <!-- is to indicate to Gradle or any Gradle module metadata file consumer  -->
  <!-- that they should prefer consuming it instead. -->
  <!-- do_not_remove: published-with-gradle-metadata -->
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.bmuschko</groupId>
  <artifactId>mid-1</artifactId>
  <version>1.0.0</version>
  <dependencies>
    <dependency>
      <groupId>com.bmuschko</groupId>
      <artifactId>low-1</artifactId>
      <version>1.0.0</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.bmuschko</groupId>
      <artifactId>low-2</artifactId>
      <version>1.0.0</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
</project>
```

### On CI, all artifacts can be published to remote repository

A CI agent executes a job for compiling code and publishing artifacts for all levels in the correct order.

```
$ ./gradlew compileJava publish
```