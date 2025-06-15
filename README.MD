# MixinTests

This is a test suite
for [Mixin](https://github.com/SpongePowered/Mixin), [Fabric Mixin](https://github.com/FabricMC/Mixin)
and [MixinExtras](https://github.com/LlamaLad7/MixinExtras).

Its purpose is to provide a unified set of tests that is helpful to the maintainers of all these projects, and primarily
to prevent regressions when making changes in any of them. This is especially important given different versions of
these projects can be combined, and testing all the combinations manually is infeasible, so instead the suite handles it
automatically.

## Test Structure

The test cases are found in [src/test](src/test). Each test is marked by a `@MixinTest` annotation and contains
one or more mixins as static inner classes. All these mixins will be applied for the test.

The target classes for these tests are found in [targets/src/main](targets/src/main). You can reuse these between tests
and add new ones as you see fit.

Each `@MixinTest` specifies a `box` (a class implementing `TextBox`) which has a `String box()` method that will
provide the output of the test.

The test outputs are found in [test-outputs](test-outputs). There is one directory per test containing an `output.txt`
(the string returned by the `box`, which must be the same for all combinations of Mixin + MixinExtras).
You can enable `testBytecode = true` in the annotation, in which case there will also be a
`classes` directory, which contains the transformed bytecode for any target classes in the test. These are used for
Golden Testing. They are checked into version control to ensure they are not changed accidentally. You should inspect
these when committing to ensure they look as expected. If changes to any of the upstream projects cause changes in these
class dumps, it is particularly critical to inspect them and make sure the changes are expected, since this lets
unexpected changes be caught easily.

The first time you run a new test it will fail and create the relevant output files. Any subsequent runs will ensure the
outputs match the output files.

## Configuring Tests

**By default, tests are run without MixinExtras**, since this greatly reduces the number of test cases and it is not
needed for plain Mixin tests (unless in a particular case you want to check it doesn't break stock behaviour).
To enable MixinExtras for a test you set `mixinExtras = TestOption.ON` in your `@MixinTest` annotation. You could also
set it to `TestOption.BOTH` if you want the test case to be run both with and without MixinExtras. There is a similar
setting for `fabricMixin`, though by default all tests are run both with and without Fabric Mixin.

`@MixinTest` also lets you configure minimum required versions for Mixin, Fabric Mixin and MixinExtras, in case your
test relies on a new feature or bug fix.

If you happen to need finer-grained control over what scenarios your test is run with, implement `RestrictedTest` and
override `boolean shouldRun(MixinVersions versions)` in your `@MixinTest` class.

## Running Tests

The Gradle `test` task will run all tests. To run a specific test, navigate to the `GeneratedMixinTests` class (may only
appear after running something) and run the method that corresponds to your test case. I plan to make an IDEA plugin in
the near future which will streamline this process.

## Test Scenarios

The relevant versions of each library are defined
in [buildSrc/src/main/kotlin/artifactVersions.kt](buildSrc/src/main/kotlin/artifactVersions.kt). The scenarios that are
considered are:
- The latest (Fabric) Mixin by itself
- The latest (Fabric) Mixin when combined with any version of MixinExtras.
- The latest MixinExtras when combined with any version of (Fabric) Mixin.

And these scenarios are filtered by your settings as explained in [Configuring Tests](#configuring-tests).