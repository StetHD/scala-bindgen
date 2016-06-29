package bindgen

// standard Scala Native imports
import scalanative.native._, stdlib._, stdio._, string._

// stdlib and our patches to it
//import scala.scalanative.native_extra._
import scala.scalanative.native.stdlib_extra._
import scala.scalanative.native.getopt._

import scala.scalanative.native.clang._
import scala.scalanative.native.clang.CXTranslationUnit_Flags._

// other imports
import scala.collection.Seq


object Main {

  import Debug._ //FIXME: remove


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
    if(args != null) {
      val argc = args.length
      val argv: Ptr[CString] = malloc(sizeof[CString] * argc).cast[Ptr[CString]]
      args.zipWithIndex.foreach { case (arg, idx) => argv(idx) = args(idx) }
      main(argc, argv)
    } else {
      puts("***** BUG: SN is not passing command line arguments to application code. *****")
      //FIXME: 
      val argc = 20
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

      //argv(17) = c"test/getopt.h"
      //+++ argv(17) = c"../llvm-sources/llvm/include/llvm-c/Core.h"
      argv(17) = c"../llvm-sources/llvm/tools/clang/include/clang-c/Index.h"

      argv(18) = c"--"
      argv(19) = c"-c"
      main(argc, argv)
    }
  }


  def main(argc: CInt, argv: Ptr[CString]): CInt = {
    printf(c"// argc=%d\n", argc)
    printf(c"// argv=%d\n", argv.cast[Int])

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

    var _argc = argc
    def loopDoubleHyphen: Unit = {
      var i = argc
      while(i > 0) {
        i = i - 1
        if(strcmp(c"--", argv(i)) == 0) { _argc = i; return }
      }
    }
    loopDoubleHyphen
    printf(c"// _argc=%d\n", _argc)

    def loopOptions: Unit = {
      while(true) {
        val c = getopt_long(_argc, argv, c"l:o:m:beT:uc:r:S", long_options, option_index)
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

    def loopArgs: Unit = {
      var i = optind
      while(i < _argc) {
        cargs.arg_files = cargs.arg_files :+ argv(i)
        i = i + 1
      }
    }
    loopArgs

    def loopClangArgs: Unit = {
      var i = _argc+1
      while(i < argc) {
        cargs.arg_clang_args = cargs.arg_clang_args :+ argv(i)
        i = i + 1
      }
    }
    loopClangArgs

    //FIXME: remove these things
    dump(c"arg_files         ", cargs.arg_files)
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

    new Visitor(cargs).process
  }

}



class Visitor(cargs: Args) {

  import Debug._ //FIXME: remove

  def process(): Int = {
    cargs.arg_files.foreach { f =>
      val file = path(f)
      dump(c"--> Processing file", file)
      val index = clang_createIndex(0, 0)
      puts(c"1")
      addr(c"index", index) // https://github.com/scala-native/scala-native/issues/215

      val clang_argc = cargs.arg_clang_args.size
      val clang_argv: Ptr[CString] = malloc(sizeof[CString] * cargs.arg_clang_args.size).cast[Ptr[CString]]
      cargs.arg_clang_args.zipWithIndex.foreach { case (p, i) => clang_argv(i) = p }
      val unit  = clang_parseTranslationUnit(index, file,
                                             clang_argv, clang_argc,
                                             null, 0.toUInt, CXTranslationUnit_None)
      puts(c"2")
      addr(c"unit", unit) // https://github.com/scala-native/scala-native/issues/215
      val cursor = clang_getTranslationUnitCursor(unit)
      puts(c"3")

      //TODO: clang_visitChildren(cursor, visitor, this)

      puts(c"4")
      clang_disposeIndex(index)
      puts(c"5")
      free(clang_argv)
      free(file)
      puts(c"6")
      puts(c"Done.")
    }
    0
  }

  @inline def path(file_name: CString): CString = {
    val resolved_name: CString = malloc(512).cast[CString]
    stdlib_extra.realpath(file_name, resolved_name)
    resolved_name
  }
}


class Args (
  var arg_files             : Seq[CString],
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
      arg_files               = Seq.empty[CString],
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


object Debug {
  private val fmt   = c"%s: %s\n"
  def dump(p: CString, s: CString): Unit         = /*if(s != NULL)*/ printf(fmt, p, s) /*else printf(fmt, p, c"--undefined--")*/
  def dump(p: CString, o: Option[CString]): Unit = if(o.isDefined) printf(fmt, p, o.get) else printf(fmt, p, c"--undefined--")
  def dump(p: CString, argc: CInt, argv: Ptr[CString]): Unit = {
    printf(c"%s:", p)
    for(i <- 1 to argc.toInt) printf(c" %s", argv(i-1))
    puts(c"")
  }
  def dump(p: CString, i: Int): Unit             = printf(c"%s: %d\n", p, i) //FIXME
  def addr(p: CString, x: Ptr[_]): Unit          = printf(c"%s: %x\n", p, x) //FIXME
  def dump(p: CString, b: Boolean): Unit         = if(b) printf(fmt, p, c"true") else printf(fmt, p, c"false")
  def dump(p: CString, l: Seq[CString]): Unit    = {
    printf(c"%s:", p)
    l.foreach(a => printf(c" %s", a))
    puts(c"")
  }
}


@extern
object stdlib_extra {
  def realpath(file_name: CString, resolved_name: CString): CString = extern
}
