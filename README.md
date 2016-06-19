## Overview

[![Join the chat at https://gitter.im/frgomes/scala-bindgen](https://badges.gitter.im/frgomes/scala-bindgen.svg)](https://gitter.im/frgomes/scala-bindgen?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**This is work in progress, in early inception phase.**

A native binding generator for the [Scala] language.
See also: [Scala Native]

scala-bindgen was originally ported from [Rust's bindgen], which was originally ported from [Clay's bindgen].

## How it works

* employs LLVM for parsing C source files
* LLVM builds an AST which represents all sources parsed
* bindgen visits the AST and generates Scala sources


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
