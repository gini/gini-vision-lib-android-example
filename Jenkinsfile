#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        SIGNING_KEYSTORE_PSW = credentials('gvl-example-app-release-keystore-password')
        SIGNING_KEY_PSW = credentials('gvl-example-app-release-key-password')
        CLIENT_CREDENTIALS = credentials('gvl-example-app-gini-api-client-credentials')
        HOCKEYAPP_API_TOKEN = credentials('gvl-example-app-hockeyapp-api-token')
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean assembleRelease -PreleaseKeystoreFile=hockeyapp.jks -PreleaseKeystorePassword="$SIGNING_KEYSTORE_PSW" -PreleaseKeyAlias=hockeyapp -PreleaseKeyPassword="$SIGNING_KEY_PSW" -PclientId=$CLIENT_CREDENTIALS_USR -PclientSecret=$CLIENT_CREDENTIALS_PSW'
                archiveArtifacts 'app/build/outputs/apk/release/app-release.apk,app/build/outputs/mapping/release/mapping.txt'
            }
        }
        stage('Upload to Hockeyapp') {
            when {
                branch 'master'
                expression {
                    def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                    return !tag.isEmpty()
                }
            }
            steps {
                step([$class: 'HockeyappRecorder', applications: [[apiToken: HOCKEYAPP_API_TOKEN, downloadAllowed: true, dsymPath: 'app/build/outputs/mapping/release/mapping.txt', filePath: 'app/build/outputs/apk/release/app-release.apk', mandatory: false, notifyTeam: false, releaseNotesMethod: [$class: 'ChangelogReleaseNotes'], uploadMethod: [$class: 'AppCreation', publicPage: false]]], debugMode: false, failGracefully: false])
            }
        }
    }
}
