package com.bmoriceau.courseExtractor.model

import java.time.LocalDateTime

case class TrackPoint(time: Option[LocalDateTime],
                      long: Option[Double],
                      lat: Option[Double],
                      alt: Option[Double],
                      cad: Option[Int],
                      watt: Option[Int],
                      speed: Option[Double])
