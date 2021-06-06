package fotoshop.util

object Extensions {
  implicit class StringExtensions(s :String) {
    def ifEmpty(f: => String): String = if (s.trim.isEmpty) f else s
  }

  implicit class IntExtensions(i: Int) {
    def between(min: Int, max: Int): Boolean = i >= min && i <= max
    def mask(m: Int) = (i & m) != 0
  }
}
