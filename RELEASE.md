# Release Process

This project uses Shipkit for the release management, including the handling of the changelog. Refer to that [project's documentation](https://github.com/mockito/shipkit) on how it works.

The variant we use is the "continuous delivery", meaning that every change that is accepted into master triggers a new release.

To get "special" versions released, send a pull request with the desired version within the `version.properties` file. Once it's merged, the desired version will be released.