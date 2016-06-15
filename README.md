## Overview

[![Join the chat at https://gitter.im/frgomes/scala-bindgen](https://badges.gitter.im/frgomes/scala-bindgen.svg)](https://gitter.im/frgomes/scala-bindgen?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**This is work in progress. It may even not compile, or fails copiously! :-(**

A native binding generator for the Scala language.

scala-bindgen was originally ported from [Rust's bindgen], which was originally ported from [Clay's bindgen].

## How it works

* employs LLVM for parsing C source files
* LLVM builds an AST which represent all sources parsed
* bindgen visits the AST and generates Scala sources

[Clay's bindgen]: http://github.com/jckarter/clay/blob/master/tools/bindgen.clay
[Rust's bindgen]: http://github.com/crabtw/rust-bindgen
