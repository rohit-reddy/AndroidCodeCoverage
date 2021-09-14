# AndroidCodeCoverage

Code Coverage for Android Testing

Code coverage is a metric that can help you understand how much of your source is tested. Code coverage analysis can only be used for the validation of test cases that are run on the source code and not for the evaluation of the software product.

How is it measured?
To calculate the code coverage percentage, simply use the following formula:

Code Coverage Percentage = (Number of lines of code executed by a testing algorithm/Total number of lines of code in a system component) * 100.

JaCoCo
Android Studio will invoke or call JaCoCo to do the code coverage reports on your unit tests but you need to perform the following tasks.

Set testCoverageEnabled to true in the build.gradle file
Change the Code Coverage Runner to JaCoCo
Run Unit Tests / Instrumentation tests with Code Coverage
View the Code Coverage
To include code coverage in your Android project, set testCoverageEnabled to true in your debug buildTypes in the build.gradle file, see Listing 5, and click on Sync Now after you make the changes.


buildTypes {
    debug {
        testCoverageEnabled true
    }
}

buildTypes {
    debug {
        testCoverageEnabled true
    }
}

Apply JaCoCo
The first thing to do is to create a separate gradle file where we write our configuration. Let’s name it jacoco.gradle and put it on gradle directory

## This is our project structure.
## We use standard Android project structure.

├── app 
│  └── src
├── gradle
│  ├── jacoco.gradle
│  └── wrapper

In this file let’s apply the JaCoCo gradle plugin for our modules


// In here we can filter out what modules that we want to cover
def coveredProject = subprojects

// configure() method takes a list as an argument and applies the configuration to the projects in this list.
configure(coveredProject) { prj -> 
 
 // Here we apply jacoco plugin to every project
 apply plugin: 'jacoco'

 // Set Jacoco version
 jacoco {
 toolVersion = "0.8.6"
 }

 // Here we create the task to generate Jacoco report
 task jacocoTestReport(type: JacocoReport) {

 // Define what type of report we should generate
 // If we don't want to process the data further, html should be enough
 reports {
 xml.enabled = true
 html.enabled = true
 }

 // Setup the .class, source, and execution directories
 final fileFilter = ['**/R.class', '**/R$*.class', '**/BuildConfig.*', '**/Manifest*.*', 'android/**/*.*']

 // Include this if you use Kotlin
 final kotlinTree = fileTree(dir: "${prj.buildDir}/tmp/kotlin-classes/", excludes: fileFilter)
 final javacTree = fileTree(dir: "${prj.buildDir}/intermediates/javac/", excludes: fileFilter)
 final mainSrc = "${prj.projectDir}/src/main/java"

 sourceDirectories.setFrom files([mainSrc])
 classDirectories.setFrom files([kotlinTree, javacTree])
 executionData.setFrom fileTree(dir: prj.buildDir, includes: [
 'jacoco/testDebugUnitTest.exec', 'outputs/code-coverage/connected/*coverage.ec'
 ])
 }
}



Now we’re ready, to run the unit test and generate the code coverage you can use



$ ./gradlew <module_name>:jacocoTestReport                                                                       
Aggregate The Coverage
Now, to create the aggregated data what you have to do is combine the jacocoReport task Let’s add some more configuration to our jacoco.gradle



apply plugin: 'jacoco'

task jacocoRootReport(type: JacocoReport, group: 'Coverage reports') {
 def projects = coveredProject

 // Here we depend on the jacocoReport task that we created before
 dependsOn(projects.jacocoTestReport)

 final source = files(projects.jacocoTestReport.sourceDirectories)

 additionalSourceDirs.setFrom source
 sourceDirectories.setFrom source

 classDirectories.setFrom files(projects.jacocoTestReport.classDirectories)
 executionData.setFrom files(projects.jacocoTestReport.executionData)
reports {
    xml.enabled = true
    html.enabled = true
    html.destination file("${rootProject.rootDir}/jacocoReport")
    xml.destination file("${rootProject.rootDir}/jacocoReport/report.xml")
}
doFirst { //noinspection GroovyAssignabilityCheck executionData.setFrom files(executionData.findAll { it.exists() }) 
} 
}

apply from: 'gradle/jacoco.gradle'
That’s it! To create the aggregated report you can run this in your terminal

$ ./gradlew jacocoRootReport
