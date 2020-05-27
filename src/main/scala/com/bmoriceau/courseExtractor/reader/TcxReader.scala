package com.bmoriceau.courseExtractor.reader

import java.time.LocalDateTime

import com.bmoriceau.courseExtractor.model.TrackPoint

import scala.xml.{Node, XML}

/**
  * Read a TCX File
  * TODO: Add other metrics when I got a file with it in it (eg: Card Frequence)
  */
object TcxReader {

  val COURSES = "Courses"
  val COURSE = "Course"
  val TRACK = "Track"
  val TRACKPOINT = "Trackpoint"
  val TIME = "Time"
  val POSITION = "Position"
  val LONGITUDE = "LongitudeDegrees"
  val LATITUDE = "LatitudeDegrees"
  val ALTITUDE = "AltitudeMeters"
  val CADENCE = "Cadence"
  val EXTENSION = "Extensions"
  val TPX = "TPX"
  val SPEED = "Speed"
  val WATTS = "Watts"

  def readTcx(tcxPath: String) = {
    val tcx = XML
      .loadFile(tcxPath)
      .child
      .filter(n => n.label == COURSES)(0)
      .child
      .filter(n => n.label == COURSE)(0)
      .child
      .filter(n => n.label == TRACK)(0)

    (tcx \ TRACKPOINT).map(trackPointToTypedClass)

  }

  def trackPointToTypedClass(n: Node): TrackPoint = {
    TrackPoint(
      time = n.child
        .find(_.label == TIME)
        .map(e => LocalDateTime.parse(e.text.dropRight(1))),
      long = n.child
        .find(_.label == POSITION)
        .flatMap(_.child.find(_.label == LONGITUDE).map(_.text.toDouble)),
      lat = n.child
        .find(_.label == POSITION)
        .flatMap(_.child.find(_.label == LATITUDE).map(_.text.toDouble)),
      alt = n.child.find(_.label == ALTITUDE).map(_.text.toDouble),
      cad = n.child.find(_.label == CADENCE).map(_.text.toInt),
      speed = n.child
        .find(_.label == EXTENSION)
        .flatMap(_.child.find(_.label == TPX))
        .flatMap(_.child.find(_.label == SPEED))
        .map(_.text.toDouble),
      watt = n.child
        .find(_.label == EXTENSION)
        .flatMap(_.child.find(_.label == TPX))
        .flatMap(_.child.find(_.label == WATTS))
        .map(_.text.toInt)
    )

  }
}
