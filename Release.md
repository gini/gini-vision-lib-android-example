Release Checklist
====

When a commit is ready to be released you only have to:

1. Tag the commit and push it
2. Start a build on our Jenkins, if it didn't start automatically

The build job uploads the app to [Hockeyapp](https://rink.hockeyapp.net/manage/apps/657872) and you can share the new version with the [Public Page](https://rink.hockeyapp.net/apps/972db965d54d43ceaa6509f63e7208c5).

> Note:
> The [Jenkinsfile](Jenkinsfile) contains the steps the build on Jenkins performs.