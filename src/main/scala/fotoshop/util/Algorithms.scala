package fotoshop.util

import fotoshop.util.Extensions.IntExtensions

import scala.collection.immutable
import scala.language.postfixOps

object Algorithms {
  def median(seq: immutable.IndexedSeq[Int]): Int = {
    seq.sorted.apply(if (seq.length.odd) seq.length / 2 else seq.length / 2 - 1 )
  }

  def average(seq: immutable.IndexedSeq[Int]): Int = {
    if (seq.isEmpty) 0 else seq.sum / seq.length
  }

  def grayscale(pixelValue: Int): Int = {
    val PIXEL_ALPHA_MASK = 0xff000000
    val avg = (List(0, 1, 2) map { byte => (pixelValue >> (byte * 8)) & 0xff } sum) / 3
    List(0, 1, 2).foldLeft(pixelValue & PIXEL_ALPHA_MASK)((b, i) => b | (avg << i * 8))
  }
}
