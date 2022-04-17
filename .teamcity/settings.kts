import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.python
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    artifactRules = "build/reports/** => build/reports"

    params {
        text("sonar.project-key", "jhipster-gradle-test", allowEmpty = false)
        text("sonar.host", "http://server:9000", allowEmpty = true)
        password("sonar.login", "credentialsJSON:9252051c-16ee-43c6-8939-628a78264766", label = "Sonar Login", description = "Sonar Login Key", display = ParameterDisplay.HIDDEN, readOnly = true)
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Gradle Unit Tests"
            tasks = "clean test"
            enableStacktrace = true
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
        gradle {
            name = "Push to sonarqube"
            tasks = "sonarqube"
            gradleParams = "-Dsonar.login=%sonar.login%"
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
        python {
            name = "Check Sonarqube"
            command = script {
                content = """
                    import requests
                    import time
                    
                    time.sleep(5)
                    response=requests.get(
                      '%sonar.host%/api/qualitygates/project_status?projectKey=%sonar.project-key%',
                      auth=('%sonar.login%','')
                    )
                    print(response)
                    jsonResponse=response.json()
                    if(response.status_code!=200):
                    	raise Exception("Error Response from Sonarqube")
                    elif (jsonResponse["projectStatus"]["status"] != "OK"):
                    	raise Exception("Quality Gate was not satisfied")
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
        }
    }
})
