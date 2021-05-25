package fotoshop.ext

object Extensions {
  implicit class StringExtensions(s :String) {
    def ifEmpty(f: => String): String = if (s.trim.isEmpty) f else s
  }
}
