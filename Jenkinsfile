#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        SIGNING_KEYSTORE_PSW = credentials('gvl-android_example-app-release-keystore-password')
        SIGNING_KEY_PSW = credentials('gvl-android_gvl-example-app-release-key-password')
        CLIENT_CREDENTIALS = credentials('gvl-android_gvl-example-app-gini-api-client-credentials2')
        APP_CENTER_API_TOKEN = credentials('gvl-android_gvl-example-app-app-center-api-token')
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean assembleRelease -PreleaseKeystoreFile=hockeyapp.jks -PreleaseKeystorePassword="$SIGNING_KEYSTORE_PSW" -PreleaseKeyAlias=hockeyapp -PreleaseKeyPassword="$SIGNING_KEY_PSW" -PclientId=$CLIENT_CREDENTIALS_USR -PclientSecret=$CLIENT_CREDENTIALS_PSW'
                archiveArtifacts 'app/build/outputs/apk/legacy/release/app-legacy-release.apk,app/build/outputs/mapping/legacy/release/mapping.txt,app/build/outputs/apk/newest/release/app-newest-release.apk,app/build/outputs/mapping/newest/release/mapping.txt'
            }
        }
        stage('Upload to Hockeyapp') {
            when {
                expression {
                    def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                    return !tag.isEmpty()
                }
            }
            steps {
                script {
                    def changelog = sh(returnStdout: true, script: "git log --format='format:- %s' --no-merges \$(git describe --abbrev=0 --tags \$(git rev-list --tags --skip=1  --max-count=1))..HEAD").trim()
                    appCenter apiToken: APP_CENTER_API_TOKEN, appName: 'Gini-Vision-Lib-Showcase', distributionGroups: 'Public', notifyTesters: true, ownerName: 'Gini-Team-Organization', pathToApp: 'app/build/outputs/apk/newest/release/app-newest-release.apk', pathToDebugSymbols: 'app/build/outputs/mapping/newestRelease/mapping.txt', releaseNotes: changelog
                    appCenter apiToken: APP_CENTER_API_TOKEN, appName: 'Gini-Vision-Lib-Showcase-legacy', distributionGroups: 'Public', notifyTesters: true, ownerName: 'Gini-Team-Organization', pathToApp: 'app/build/outputs/apk/legacy/release/app-legacy-release.apk', pathToDebugSymbols: 'app/build/outputs/mapping/legacyRelease/mapping.txt', releaseNotes: changelog
                }
            }
        }
    }
}
