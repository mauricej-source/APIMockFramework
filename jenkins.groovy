#!/usr/bin/env groovy

def ciBaseImage = '/compozed/ci-base-se:latest'
def ciDockerRegistry
def stopBuild
def commit
def NON_PROD_ENVIRONMENT = 'insertNONPRODNameHere'

node('linux_agent') {
    env.FULL_VERSION = "1.0.${env.BUILD_NUMBER}"
    cleanWs()
    checkout scm
    ciDockerRegistry = env.DOCKER_REGISTRY
    env.PROJECT_NAME = 'wmf'
    env.ORG_NAME = 'WIREMOCK'
    commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    env.GITHUB_COMMIT_ID = commit?.trim()

    docker.withRegistry('https://' + env.DOCKER_REGISTRY, 'userNameHere') {
        docker.image(ciDockerRegistry + ciBaseImage).inside('--privileged') {
            withEnv([
                    'GRADLE_USER_HOME=~/.gradle'
            ]) {
                sh 'chmod +x ./gradlew'

                withCredentials(
                        [usernamePassword(credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER')]
                ) {
                    stage('Build') {
                        sh './gradlew clean build'
                    }
                }

                withCredentials(
                        [usernamePassword(credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER')]
                ) {
                    stage('Quality Check Sonar') {
                        sh './gradlew sonarqube -i'
                    }
                }

                withCredentials(
                        [usernamePassword(credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER')]
                ) {
                    stage('Artifactory Push') {
                        sh './gradlew artifactoryPublish'
                    }
                }
            }
        }
    }
}

//-------------------------------------------------------------------------
//DEV Environment
//-------------------------------------------------------------------------
stopBuild = confirmNextStep('deploy to DEV?', 4)
if (stopBuild) {
    return
}

conveyorDeploy('dev', NON_PROD_ENVIRONMENT, 'AuthSecretDEV')
checkpoint "after DEV deployment"

//Stop NONProduction Environments
conveyorStop('dev', NON_PROD_ENVIRONMENT)

stopBuild = confirmNextStep('deploy to UAT?', 4)
if (stopBuild) {
    //Restart NONProduction Environments
    conveyorStart('dev', NON_PROD_ENVIRONMENT)
      
    return
}

//-------------------------------------------------------------------------
//UAT Environment
//-------------------------------------------------------------------------
conveyorDeploy('uat', NON_PROD_ENVIRONMENT, 'AuthSecretUAT')
checkpoint "after UAT deployment"

//Stop NONProduction Environments
conveyorStop('uat', NON_PROD_ENVIRONMENT)

//Restart NONProduction Environments
conveyorStart('dev', NON_PROD_ENVIRONMENT)

stopBuild = confirmNextStep('deploy to INT?', 4)
if (stopBuild) {
    //Restart NONProduction Environments
    conveyorStart('uat', NON_PROD_ENVIRONMENT)
      
    return
}

//-------------------------------------------------------------------------
//INT Environment
//-------------------------------------------------------------------------
conveyorDeploy('int', NON_PROD_ENVIRONMENT, 'AuthSecretINT')
checkpoint "after INT deployment"
//stage('Integration test') {
//    withEnv(['GRADLE_USER_HOME=~/.gradle']) {
//        //run Integration tests here...
//    }
//}

//Stop NONProduction Environments
conveyorStop('int', NON_PROD_ENVIRONMENT)

//Restart NONProduction Environments
conveyorStart('uat', NON_PROD_ENVIRONMENT)

stopBuild = confirmNextStep('deploy to STAGING?', 4)
if (stopBuild) {
    //Restart NONProduction Environments
    conveyorStart('int', NON_PROD_ENVIRONMENT)

    return
}

//-------------------------------------------------------------------------
//STAGING Environment
//-------------------------------------------------------------------------
conveyorDeploy('staging', NON_PROD_ENVIRONMENT, 'AuthSecretSTAGING')
checkpoint "after STAGING deployment"

//Stop NONProduction Environments
conveyorStop('staging', NON_PROD_ENVIRONMENT)

//Restart NONProduction Environments
conveyorStart('int', NON_PROD_ENVIRONMENT)

//Restart NONProduction Environments
conveyorStart('staging', NON_PROD_ENVIRONMENT)

return
//-------------------------------------------------------------------------

def conveyorDeploy(space, environment, authSecret, numberOfInstances = 1) {
    node('linux_agent') {
        def spaceUpperCase = space.toUpperCase()
        def spaceLowerCase = space.toLowerCase()
        def prodDeploy = spaceUpperCase == 'PROD'

        withCredentials([
                string([credentialsId: authSecret, variable: 'PRIMARY_CREDENTIAL']),
                usernamePassword([credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER'])
        ]) {
            stage("Deploy ${spaceUpperCase}") {
                step([
                        $class          : 'ConveyorJenkinsPlugin',
                        organization    : 'WIREMOCK',
                        space           : "${spaceUpperCase}",
                        environment     : "${environment}",
                        applicationName : "wmf-${spaceLowerCase}",
                        artifactURL     : "artifactoryURLHere${env.FULL_VERSION}/wmf-${env.FULL_VERSION}.jar",
                        serviceNowGroup : 'insertServiceNowGroup',
                        serviceNowUserID: "insertServiceNowUserID",
                        username        : prodDeploy ? "${env.USERNAME}" : "${env.SYS_USER}",
                        password        : prodDeploy ? "${env.PASSWORD}" : "${env.SYS_PASSWORD}",
                        manifest        : """
                            applications:
                            - memory: 1024M
                              instances: '${numberOfInstances}'
                              env:
                                SPRING_PROFILES_ACTIVE: '${spaceLowerCase}'
                                APPFABRIC_SECURITY_CREDENTIALS_PRIMARY: '${PRIMARY_CREDENTIAL}'
                              buildpacks:
                                - java_buildpack
                        """
                ])
            }
        }
    }
}

def conveyorStop(space, environment) {
    node('linux_agent') {
        def spaceUpperCase = space.toUpperCase()
        def spaceLowerCase = space.toLowerCase()

        withCredentials([
                usernamePassword([credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER'])
        ]) {
            step([
                    $class         : 'ConveyorStop',
                    applicationName: "wmf-${spaceLowerCase}",
                    environment    : "${environment}",
                    organization   : 'WIREMOCK',
                    serviceNowGroup: 'insertServiceNowGroup',
                    space          : "${spaceUpperCase}",
                    username       : "${env.SYS_USER}",
                    password       : "${env.SYS_PASSWORD}"
            ])
        }
    }
}

def conveyorStart(space, environment) {
    node('linux_agent') {
        def spaceUpperCase = space.toUpperCase()
        def spaceLowerCase = space.toLowerCase()

        withCredentials([
                usernamePassword([credentialsId: 'userNameHere', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER'])
        ]) {
            step([
                    $class         : 'ConveyorStart',
                    applicationName: "wmf-${spaceLowerCase}",
                    environment    : "${environment}",
                    organization   : 'WIREMOCK',
                    serviceNowGroup: 'insertServiceNowGroup',
                    space          : "${spaceUpperCase}",
                    username       : "${env.SYS_USER}",
                    password       : "${env.SYS_PASSWORD}"
            ])
        }
    }
}
