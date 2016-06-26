package scala.scalanative
package native

import scalanative.native._, stdlib._, stdio._

object stdlib_extra {
  implicit def string2cstring(str: String): CString = {
    val ptr = stdlib.malloc(str.length + 1).cast[Ptr[Byte]]
    var i = 0
    while (i < str.length) {
      ptr(i) = str.charAt(i).toByte
      i += 1
    }
    ptr(i) = 0
    ptr
  }
}
