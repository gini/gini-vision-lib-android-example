#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        SIGNING_KEYSTORE_PSW = credentials('gvl-android_gvl-example-appcenter-release-keystore-password')
        SIGNING_KEY_PSW = credentials('gvl-android_gvl-example-appcenter-release-key-password')
        CLIENT_CREDENTIALS = credentials('gvl-android_gvl-example-app-gini-api-client-return-assistant-credentials')
        APP_CENTER_API_TOKEN = credentials('gvl-android_gvl-example-app-app-center-api-token')
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean assembleRelease -PreleaseKeystoreFile=hockeyappcenterkeystore.jks -PreleaseKeystorePassword="$SIGNING_KEYSTORE_PSW" -PreleaseKeyAlias=hockeyapp -PreleaseKeyPassword="$SIGNING_KEY_PSW" -PclientId=$CLIENT_CREDENTIALS_USR -PclientSecret=$CLIENT_CREDENTIALS_PSW'
                archiveArtifacts 'app/build/outputs/apk/newest/release/app-newest-release.apk,app/build/outputs/mapping/newest/release/mapping.txt'
            }
        }
        stage('Upload to Hockeyapp') {
            when {
                expression {
                    def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                    return status == 0
                }
            }
            steps {
                script {
                    def changelog = sh(returnStdout: true, script: "git log --format='format:- %s' --no-merges \$(git describe --abbrev=0 --tags \$(git rev-list --tags --skip=1  --max-count=1))..HEAD").trim()
                    appCenter apiToken: APP_CENTER_API_TOKEN, appName: 'Gini-Vision-Lib-Showcase', distributionGroups: 'Public', notifyTesters: true, ownerName: 'Gini-Team-Organization', pathToApp: 'app/build/outputs/apk/newest/release/app-newest-release.apk', pathToDebugSymbols: 'app/build/outputs/mapping/newestRelease/mapping.txt', releaseNotes: changelog
                }
            }
        }
    }
}
