Release Documentation
====

When a commit is ready to be released you only have to:

1. Tag the commit with a semver version (see below) and push it
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

The build job uploads the app to [App Center](https://appcenter.ms/orgs/Gini-Team-Organization/apps/Gini-Vision-Lib-Showcase) and you can share the new version with the [Public Page](https://install.appcenter.ms/orgs/gini-team-organization/apps/gini-vision-lib-showcase/distribution_groups/public).

> Note:
> The [Jenkinsfile](Jenkinsfile) contains the steps the build on Jenkins performs.