#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        SIGNING_KEYSTORE_PSW = credentials('gvl-android_example-app-release-keystore-password')
        SIGNING_KEY_PSW = credentials('gvl-android_gvl-example-app-release-key-password')
        CLIENT_CREDENTIALS = credentials('gvl-android_gvl-example-app-gini-api-client-credentials')
        HOCKEYAPP_API_TOKEN = credentials('gvl-android_gvl-example-app-hockeyapp-api-token')
        HOCKEYAPP_API_TOKEN_LEGACY = credentials('gvl-android_gvl-example-legacy-app-hockeyapp-api-token')
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
                sh "git log --format='format:- %s' --no-merges \$(git describe --abbrev=0 --tags \$(git rev-list --tags --skip=1  --max-count=1))..HEAD >> changelog.md"
                step([$class: 'HockeyappRecorder', applications: [[apiToken: HOCKEYAPP_API_TOKEN_LEGACY, downloadAllowed: true, dsymPath: 'app/build/outputs/mapping/legacy/release/mapping.txt', filePath: 'app/build/outputs/apk/legacy/release/app-legacy-release.apk', mandatory: false, notifyTeam: false, releaseNotesMethod: [$class: 'FileReleaseNotes', fileName: 'changelog.md', isMarkdown: true], uploadMethod: [$class: 'AppCreation', publicPage: true]]], debugMode: false, failGracefully: false])
                step([$class: 'HockeyappRecorder', applications: [[apiToken: HOCKEYAPP_API_TOKEN, downloadAllowed: true, dsymPath: 'app/build/outputs/mapping/newest/release/mapping.txt', filePath: 'app/build/outputs/apk/newest/release/app-newest-release.apk', mandatory: false, notifyTeam: true, releaseNotesMethod: [$class: 'FileReleaseNotes', fileName: 'changelog.md', isMarkdown: true], uploadMethod: [$class: 'AppCreation', publicPage: true]]], debugMode: false, failGracefully: false])
            }
            post {
                always {
                    sh "rm changelog.md || true"
                }
            }
        }
    }
}
