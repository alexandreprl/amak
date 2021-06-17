This repository contains a framework made to facilitate the development of multi-agent system. 

Examples are available in the package fr.irit.smac.amak.examples.

This version is still under active development. Please refer to [version 1.5.3](https://bitbucket.org/perlesa/amak/src/1.5.3/) for the last stable version.

# How to use Amak (with Intellij Idea)

Please note that Amak uses Gradle only since version 2 and therefore, the following steps are not application for versions before version 2. Please refer to the README file of previous versions if needed.

- Create a new project
    - Select Gradle Project then Java in "additional libraries"
- Modify settings.gradle
  - add
    ```
    sourceControl {
        gitRepository("https://bitbucket.org/perlesa/amak.git") {
            producesModule("fr.irit.smac.amak:amak")
        }
    }
    ```
- Modify build.gradle
  - Specify Amak branch
    ```
    dependencies {
      implementation('fr.irit.smac.amak:amak') {
            version {
                branch = 'BRANCH_NAME'
            }
        }
    }
    ```
  - or a specific tag

    ```
    dependencies {
      implementation('fr.irit.smac.amak:amak:TAG_NAME')
    }
    ```
    
- Refresh gradle