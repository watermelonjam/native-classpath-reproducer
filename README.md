Shows that classpath resource folder inclusion via `quarkus.native.resources.includes`
has some odd behaviour in a native image.  The folder seems to exist but is empty.  Even
though using `strings target/native-classpath-reproducer-1.0.0-SNAPSHOT-runner` 
to dump contents shows that the filenames are there and file contents are there... somehwere.

Use `mvn verify -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true`
to reproduce.
