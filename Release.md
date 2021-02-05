Release Documentation
====

When a commit is ready to be released you only have to:

1. Increment app version name and tag the commit with a semver version (see below) and push it
2. Start a build on our Jenkins, if it didn't start automatically

App Versioning
----

We use [semantic versioning](https://semver.org/). In short the version has three numbers: 
1. The first one is the major version number, which is increased when significant or braking changes were introduced.
2. The second one is the minor version number, which is increased whenever incremental changes were added. This number is zeroed when the major version has been incremented.
3. The third one is the patch version number, which is increased when small changes like bugfixes were added. This number is zeroed when the minor version has been incremented.

For example version `1.2.3` would mean we are at the first major release from this one at the second minor release from which we are at the third patch release.

Jenkins Pipeline Build Job
----

The build job uploads the app to [Hockeyapp](https://rink.hockeyapp.net/manage/apps/657872) and you can share the new version with the [Public Page](https://rink.hockeyapp.net/apps/972db965d54d43ceaa6509f63e7208c5).

> Note:
> The [Jenkinsfile](Jenkinsfile) contains the steps the build on Jenkins performs.