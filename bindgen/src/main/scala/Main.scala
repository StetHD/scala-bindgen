package bindgen

// standard Scala Native imports
import scalanative.native._, stdlib._, stdio._

// stdlib and our patches to it
//import scala.scalanative.native_extra._
import scala.scalanative.native.stdlib_extra._
import scala.scalanative.native.getopt._


// other imports
import scala.collection.Seq


object Main {
  def usage: Unit = {
    puts(c"Generate C bindings for Scala Native.")
  }

  //+++ final private val USAGE =
  //+++   s"""|Generate C bindings for Scala Native.
  //+++       |Usage:
  //+++       |  bindgen [options] <file> [-- <clang-args>...]
  //+++       |  bindgen (-h | --help)
  //+++       |Options:
  //+++       |  <clang-args>                 Options passed directly to clang.
  //+++       |  -h, --help                   Display this help message.
  //+++       |  --link=<library>             Link to a dynamic library, can be provided multiple times.
  //+++       |                               <library> is in the format `[kind=]lib`, where `kind` is
  //+++       |                               one of `static`, `dynamic` or `framework`.
  //+++       |  --output=<output>            Write bindings to <output> (- is stdout).
  //+++       |                               [default: -]
  //+++       |  --match=<name>               Only output bindings for definitions from files
  //+++       |                               whose name contains <name>
  //+++       |                               If multiple -match options are provided, files
  //+++       |                               matching any rule are bound to.
  //+++       |  --builtins                   Output bindings for builtin definitions
  //+++       |                               (for example __builtin_va_list)
  //+++       |  --emit-clang-ast             Output the ast (for debugging purposes)
  //+++       |  --override-enum-type=<type>  Override enum type, type name could be
  //+++       |                                 uchar
  //+++       |                                 schar
  //+++       |                                 ushort
  //+++       |                                 sshort
  //+++       |                                 uint
  //+++       |                                 sint
  //+++       |                                 ulong
  //+++       |                                 slong
  //+++       |                                 ulonglong
  //+++       |                                 slonglong
  //+++       |  --use-core                  Use `core` as a base crate for `Option` and such.
  //+++       |                              See also `--ctypes-prefix`.
  //+++       |  --ctypes-prefix=<prefix>    Use this prefix for all the types in the generated
  //+++       |                              code.
  //+++       |                              [default: std::os::raw]
  //+++       |  --remove-prefix=<prefix>    Prefix to remove from all the symbols, like
  //+++       |                              `libfoo_`. The removal is case-insensitive.
  //+++       |  --no-scala-enums            Convert C enums to Scala constants instead of enums.
  //+++       |  --dont-convert-floats       Disables the convertion of C `float` and `double`
  //+++       |                              to Scala `f32` and `f64`.
  //+++       |""".stripMargin

  def main(args: Array[String]): Unit = {
    cli(args)
  }


  def cli(args: Array[String]): Args = {
    //val argc = args.length
    //val argv: Ptr[CString] = malloc(sizeof[CString] * argc).cast[Ptr[CString]]
    //args.zipWithIndex.foreach { case (arg, idx) => argv(idx) = args(idx) }

    //FIXME: this is hardcoded stuff for the time being, for debugging purposes
    val argc = 19
    val argv: Ptr[CString] = malloc(sizeof[CString] * argc).cast[Ptr[CString]]
    argv( 0) = c"scala-bindgen"
    argv( 1) = c"--output"             ; argv( 2) = c"(output)"
    argv( 3) = c"--match"              ; argv( 4) = c"(match)"
    argv( 5) = c"--builtins"
    argv( 6) = c"--emit-clang-ast"
    argv( 7) = c"--override-enum-type" ; argv( 8) = c"(override-enum-type)"
    argv( 9) = c"--use-core"
    argv(10) = c"--ctypes-prefix"      ; argv(11) = c"(ctypes-prefix)"
    argv(12) = c"--remove-prefix"      ; argv(13) = c"(remove-prefix)"
    argv(14) = c"--no-scala-enums"
    argv(15) = c"--link"               ; argv(16) = c"(link)"
    argv(17) = c"test/getopt.h"
    argv(18) = c"../llvm-sources/llvm/include/llvm-c/Core.h"
    //+++ argv(18) = c"--"
    //+++ argv(19) = c"(clang-arg1)"
    //+++ argv(20) = c"(clang-arg2)"
    //+++ argv(21) = c"(clang-arg3)"

    val long_options = malloc(sizeof[option] * 12).cast[Ptr[option]]
    long_options( 0) = new option(c"help",               0, null,   'h')
    long_options( 1) = new option(c"link",               1, null,   'l')
    long_options( 2) = new option(c"output",             1, null,   'o')
    long_options( 3) = new option(c"match",              1, null,   'm')
    long_options( 4) = new option(c"builtins",           0, null,   'b')
    long_options( 5) = new option(c"emit-clang-ast",     0, null,   'e')
    long_options( 6) = new option(c"override-enum-type", 1, null,   'T')
    long_options( 7) = new option(c"use-core",           0, null,   'u')
    long_options( 8) = new option(c"ctypes-prefix",      1, null,   'c')
    long_options( 9) = new option(c"remove-prefix",      1, null,   'r')
    long_options(10) = new option(c"no-scala-enums",     0, null,   'S')
    long_options(11) = new option(null,                  0, null,     0)

    var cargs = Args()
    val option_index: Ptr[CInt] = stackalloc[CInt]

    def loopOptions: Unit = {
      while(true) {
        val c = getopt_long(argc, argv, c"l:o:m:beT:uc:r:S", long_options, option_index)
        c match {
          case 'l' => cargs.opt_link               = cargs.opt_link :+ optarg //TODO: new LinkInfo(optarg, LinkType.Dynamic)
          case 'o' => cargs.opt_output             = Some(optarg)
          case 'm' => cargs.opt_match              = Some(optarg)
          case 'b' => cargs.opt_builtins           = true
          case 'e' => cargs.opt_emit_clang_ast     = true
          case 'T' => cargs.opt_override_enum_type = Some(optarg)
          case 'c' => cargs.opt_ctypes_prefix      = Some(optarg)
          case 'u' => cargs.opt_use_core           = true
          case 'r' => cargs.opt_remove_prefix      = Some(optarg)
          case 'S' => cargs.opt_no_scala_enums     = true
          case 'h' => usage; return
          case '?' => usage; return
          case -1  => return
          case _   => usage; return
        }
      }
    }
    loopOptions

    def loopArguments: Unit = {
      var i = optind
      while(i < argc) {
        cargs.arg_file = cargs.arg_file :+ argv(optind)
        i = i + 1
      }
    }
    loopArguments

    dump(c"arg_file          ", cargs.arg_file)
    dump(c"clang_args        ", cargs.arg_clang_args)
    dump(c"link              ", cargs.opt_link)
    dump(c"output            ", cargs.opt_output)
    dump(c"match             ", cargs.opt_match)
    dump(c"builtins          ", cargs.opt_builtins)
    dump(c"clang_ast         ", cargs.opt_emit_clang_ast)
    dump(c"override_enum_type", cargs.opt_override_enum_type)
    dump(c"ctypes_prefix     ", cargs.opt_ctypes_prefix)
    dump(c"use_core          ", cargs.opt_use_core)
    dump(c"remove_prefix     ", cargs.opt_remove_prefix)
    dump(c"no_scala_enums    ", cargs.opt_no_scala_enums)
    puts(c"Done.")

    cargs
  }


  private val fmt   = c"%s: %s\n"
  private def dump(p: CString, o: Option[CString]): Unit = if(o.isDefined) printf(fmt, p, o.get) else printf(fmt, p, c"--undefined--")
  private def dump(p: CString, b: Boolean): Unit         = if(b) printf(fmt, p, c"true") else printf(fmt, p, c"false")
  private def dump(p: CString, l: Seq[CString]): Unit    = {
    printf(c"%s:", p)
    l.foreach(a => printf(c" %s", a))
    puts(c"")
  }

}


class Args (
  var arg_file              : Seq[CString],
  var arg_clang_args        : Seq[CString],
  var opt_link              : Seq[CString],
  var opt_output            : Option[CString],
  var opt_match             : Option[CString],
  var opt_builtins          : Boolean,
  var opt_emit_clang_ast    : Boolean,
  var opt_override_enum_type: Option[CString],
  var opt_ctypes_prefix     : Option[CString],
  var opt_use_core          : Boolean,
  var opt_remove_prefix     : Option[CString],
  var opt_no_scala_enums    : Boolean
)
object Args {
  def apply() =
    new Args(
      arg_file                = Seq.empty[CString],
      arg_clang_args          = Seq.empty[CString],
      opt_link                = Seq.empty[CString],
      opt_output              = None,
      opt_match               = None,
      opt_builtins            = false,
      opt_emit_clang_ast      = false,
      opt_override_enum_type  = None,
      opt_ctypes_prefix       = None,
      opt_use_core            = false,
      opt_remove_prefix       = None,
      opt_no_scala_enums      = false)
}


//@struct
//class LinkInfo(val link: CString, val `type`: LinkType.enum)


@struct
object LinkType {
  type enum = Int
  final val Static    : LinkType.enum = 1 // Do a static link to the library.
  final val Dynamic   : LinkType.enum = 2 // Do a dynamic link to the library.
  final val Framework : LinkType.enum = 3 // Link to a MacOS Framework.
}


/** Trait used internaly to log things with context like the C file line number.*/
trait Logger { //FIXME : std::fmt::Debug {
  def error(msg: String) = println(s"ERROR: ${msg}")
  def warn (msg: String) = println(s"WARN: ${msg}")
  def info (msg: String) = println(s"INFO: ${msg}")
  def debug(msg: String) = println(s"DEBUG: ${msg}")
}


/** Contains the generated code.*/
class Bindings {
  val module    : Any = null // ast::Mod,
  val attributes: Any = null // Vec<ast::Attribute>,
}
