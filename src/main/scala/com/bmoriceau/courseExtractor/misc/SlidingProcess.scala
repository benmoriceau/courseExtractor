package com.bmoriceau.courseExtractor.misc

import com.bmoriceau.courseExtractor.model.{SportMetadata, TrackPoint}

object SlidingProcess {
  def metadataOverSlide(trackpoint: Seq[TrackPoint],
                        size: Int): Seq[SportMetadata] = {
    var i = 0;

    var res = Seq.empty[SportMetadata]

    while (i + size < trackpoint.length) {
      val slice = trackpoint.slice(i, i + size)

      // TODO: Filter & SLICE by Time
      val aggSlice = slice.foldLeft(
        SportMetadata(
          speed = 0,
          cad = 0,
          watt = 0,
          minElevation = Double.MaxValue,
          maxElevation = Double.MinValue
        )
      ) {
        case (
            agg: SportMetadata,
            TrackPoint(_, _, _, None, cad, watt, speed)
            ) =>
          agg.copy(
            speed = speed.getOrElse(0.0) + agg.speed,
            cad = cad.getOrElse(0) + agg.cad,
            watt = watt.getOrElse(0) + agg.watt
          )
        case (
            agg: SportMetadata,
            TrackPoint(_, _, _, Some(alt), Some(cad), Some(watt), Some(speed))
            ) if (alt < agg.minElevation && alt > agg.maxElevation) =>
          SportMetadata(
            speed = speed + agg.speed,
            cad = cad + agg.cad,
            watt = watt + agg.watt,
            minElevation = alt,
            maxElevation = alt
          )
        case (
            agg: SportMetadata,
            TrackPoint(_, _, _, Some(alt), Some(cad), Some(watt), Some(speed))
            ) if (alt < agg.minElevation) =>
          agg.copy(
            speed = speed + agg.speed,
            cad = cad + agg.cad,
            watt = watt + agg.watt,
            minElevation = alt
          )
        case (
            agg: SportMetadata,
            TrackPoint(_, _, _, Some(alt), Some(cad), Some(watt), Some(speed))
            ) if (alt > agg.maxElevation) =>
          agg.copy(
            speed = speed + agg.speed,
            cad = cad + agg.cad,
            watt = watt + agg.watt,
            minElevation = alt
          )
        case (agg: SportMetadata, tp: TrackPoint) =>
          agg // Incomplete point, ignore
      }

      res = res :+ aggSlice
      i += 1
    }
    res
  }
}
