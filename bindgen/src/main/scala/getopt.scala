package scala.scalanative
package unistd

import native._


@extern
object getopt {
  val optarg: CString = extern
  val optind: CInt = extern
  val optopt: CInt = extern

@struct
class option(
  val name   : CString,
  val has_arg: CInt,
  val flag   : Ptr[CInt],
  val `val`  : CInt
)

  def getopt(
    argc     : CInt,
    argv     : Array[Ptr[CString]],
    optstring: Array[Ptr[CString]]): CInt = extern

  def getopt_long(
    argc     : CInt,
    argv     : Array[Ptr[CString]],
    optstring: CString,
    longopts : Ptr[option],
    longindex: Ptr[CInt]): CInt = extern

  def getopt_long_only(
    argc     : CInt,
    argv     : Array[Ptr[CString]],
    optstring: CString,
    longopts : Ptr[option],
    longindex: Ptr[CInt]): CInt = extern
}
