## Overview

[![Join the chat at https://gitter.im/frgomes/scala-bindgen](https://badges.gitter.im/frgomes/scala-bindgen.svg)](https://gitter.im/frgomes/scala-bindgen?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**This is work in progress, in early inception phase.**

A native binding generator for the [Scala] language.
See also: [Scala Native]

scala-bindgen was originally ported from [Rust's bindgen], which was originally ported from [Clay's bindgen].

## For the impatient

* For convenience, please define LLVM_HOME similar to the example below:

    $ export LLVM_HOME=/opt/developer/clang+llvm-3.8.0-x86_64-linux-gnu-debian8

* Make sure you define LD_LIBRARY_PATH:

    $ export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$LLVM_HOME/lib

* Now you can build and run

    $ sbt nativeLink
    $ bindgen/target/scala-2.11/bindgen-out


## How it works

In a nutshell, ``scala-bindgen`` lays on the shoulders of the giant [Clang] compiler (from [LLVM] infrastructure) for achieving the task of transforming 
``C`` header files into ``Scala`` bindings. For examples on how ``Scala`` bindings look like, please see [stdlib.scala].

* ``scala-bindgen`` employs ``Clang`` (from LLVM infrastructure) for parsing ``C`` header files;
* ``Clang`` builds an AST which represents all sources parsed;
* ``scala-bindgen`` visits the AST and generates Scala bindings.

At the moment ``scala-bindgen`` only generates bindings for ``C`` language.


### Usage

Due to pending items in Scala Native, we currenly cannot obtain parameters from the command line.
For this reason, the code has some hardcoded arguments, for testing purposes.

But anyway... when command line arguments become wired properly, it will be like this:

    $ scala-bindgen [arguments] <header-file> [-- [clang-arguments]]

### Examples

The necessary help screen:

    $ scala-bindgen --help

Generate bindings for ``getopt.h`` on a given output directory:

    $ mkdir -p fake/src
    $ scala-bindgen -O fake/src test/getopt.h

Same as before, but passes the include directory to ``Clang``:

    $ mkdir -p fake/src
    $ scala-bindgen -O fake/src getopt.h -- -I test

Tries to find ``stdlib.h`` in the current directory and, if not successful, tries on "well known locations".
Generates bindings on the current directory, on file ``stdlib.scala``.
Passes ``--verbose`` flag a number of times so that it increases the logging level, displaying which "well known locations" are those:

    $ scala-bindgen -vvv stdlib.h

Same as above, but defines a package name for the generated bindings:

    $ scala-bindgen --package scala.scalanative.my.bindings stdlib.h

Same as above, but defines an enclosing object named ``my_stdlib``, instead of ``stdlib``:

    $ scala-bindgen --package scala.scalanative.my.bindings --name my_stdlib stdlib.h

Passes arguments to Clang after the double hyphen ``--``:

    $ scala-bindgen rational_solver.h -- -DMAX_INTERATIONS=100 -DTOLERANCE=1.0E-10 \
                                         -I $RATIONAL_INCLUDE \
                                         -L $RATIONAL_LIB -lrational

## Community

 * Have a question? Ask it on [Stack Overflow with tag scala-bindgen].
 * Want to chat? Join our [Gitter chat channel].
 * Found a bug or want to propose a new feature? Open [an issue on Github].

## License

Scala Native is distributed under [the Scala license].


[Stack Overflow with tag scala-bindgen]: http://stackoverflow.com/questions/tagged/scala-bindgen
[Gitter chat channel]: https://gitter.im/frgomes/scala-bindgen
[an issue on Github]: https://github.com/frgomes/scala-bindgen/issues
[the Scala license]: https://github.com/frgomes/scala-bindgen/blob/master/LICENSE

[Scala]: http://scala-lang.org
[Scala Native]: http://github.com/scala-native/scala-native
[Clay's bindgen]: http://github.com/jckarter/clay/blob/master/tools/bindgen.clay
[Rust's bindgen]: http://github.com/crabtw/rust-bindgen

[stdlib.scala]: http://github.com/scala-native/scala-native/blob/master/nativelib/src/main/scala/scala/scalanative/native/stdlib.scala
