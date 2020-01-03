import sbt.Keys._

// Scalac 2.13 ready
object CompilerOpts {

  val javacFlags = Seq(
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-source", "1.8",                    // Specifies the version of source code accepted.
    "-g",                                // Generate all debugging information, including local variables.
    "-g:vars",                           // Generate only some kinds of debugging information - Local variable debugging information.
    "-deprecation"                       // Show a description of each use or override of a deprecated member or class.
  )

  val scalacFlags = Seq(
    "-target:jvm-1.8",
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
    "-language:higherKinds",             // Allow higher-kinded types
    "-language:postfixOps",              // Enable postfix operators.
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.

    "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.

    "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
    "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
    "-Xlint:option-implicit",            // Option.apply used implicit view.
    "-Xlint:package-object-classes",     // Class or object defined in package object.
    "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.

    "-Ywarn-dead-code",                  // Warn when dead code is identified.
    "-Ywarn-numeric-widen"               // Warn when numerics are widened.
  )
 
}
