package com.bmoriceau.courseExtractor

import com.bmoriceau.courseExtractor.misc.SlidingProcess
import com.bmoriceau.courseExtractor.reader.TcxReader

object Main {

  def main(args: Array[String]): Unit = {

    val testOneMinute =
      SlidingProcess.metadataOverSlide(TcxReader.readTcx("test.xml"), 60)

    val maxAvgWatt = testOneMinute.map(_.watt).max / 60
    val maxAvgSpeed = testOneMinute.map(_.speed).max / 60
    val maxAvgSpeedKmh = maxAvgSpeed * 3.6
    val maxAvgCad = testOneMinute.map(_.cad).max / 60

    println(s"""
        | max Average Watt = $maxAvgWatt
        | max Average Speed = $maxAvgSpeed (m/s) $maxAvgSpeedKmh (km/h)
        | max Average Cadence = $maxAvgCad
        |""".stripMargin)
  }

}
