#!/usr/bin/env groovy

node('linux_agent') {
    cleanWs()
    env.FULL_VERSION = "1.0.${env.BUILD_NUMBER_INPUT}"

    def stopBuild = confirmNextStepWithCredentials('deploy to PROD?', 4)
    if (stopBuild) {
        return
    }

    withCredentials([
            string([credentialsId: 'AuthSecretPROD', variable: 'PRIMARY_CREDENTIAL'])
    ]) {
        stage("Deploy PROD") {
            step([
                    $class          : 'ConveyorJenkinsPlugin',
                    organization    : 'WIREMOCK',
                    space           : 'PROD',
                    environment     : 'insertEnvironmentNameHere',
                    applicationName : 'wmf-prod',
                    artifactURL     : "artifactoryURLHere${env.FULL_VERSION}/wmf-${env.FULL_VERSION}.jar",
                    serviceNowGroup : 'insertServiceNowGroupHere',
                    serviceNowUserID: "${insertServiceNowUserID}",
                    username        : "${env.USERNAME}",
                    password        : "${env.PASSWORD}",
                    manifest        : """
                            applications:
                            - memory: 1024M
                              instances: 2
                              env:
                                APPFABRIC_SECURITY_CREDENTIALS_PRIMARY: '${PRIMARY_CREDENTIAL}'
                              buildpacks:
                                - java_buildpack
                        """
            ])
        }

        stage("Production Number") {
            println "Build Version: " + env.FULL_VERSION
        }
    }
}
