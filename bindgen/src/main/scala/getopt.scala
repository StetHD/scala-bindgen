package scala.scalanative
package native


//@link("c")
//@name("stdlib")
@extern
object getopt {
  var optarg: CString = extern
  var optind: CInt    = extern
  var opterr: CInt    = extern
  var optopt: CInt    = extern

  def getopt(argc: CInt, argv: Ptr[CString], optstring: CString): CInt = extern

  def getopt_long     (argc: CInt, argv: Ptr[CString], optstring: CString, longopts: Ptr[option], longindex: Ptr[CInt]): CInt = extern
  def getopt_long_only(argc: CInt, argv: Ptr[CString], optstring: CString, longopts: Ptr[option], longindex: Ptr[CInt]): CInt = extern

  @struct
  class option(val name:    CString,
               val has_arg: CInt,
               val flag:    Ptr[CInt],
               val `val`:   CInt)
}
