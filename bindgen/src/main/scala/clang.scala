package bindgen

//FIXME: https://github.com/frgomes/scala-bindgen/issues/6
class Clang {
  def c_search_paths(): Seq[String] = Seq.empty[String]
}
object Clang {
  def find(arg: Option[String]): Option[Clang] = None
}
