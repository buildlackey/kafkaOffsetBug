#!/bin/sh
exec scala "$0" "$@"
!#

import java.text.SimpleDateFormat
import java.util.{Locale, Calendar, Date}

object ToEpoch {
  def main(args: Array[String]) {
      val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
      val formatter: SimpleDateFormat = new SimpleDateFormat(datePattern, Locale.US)
      //val timestamp = "2017-03-10T14:19:39.966-0800"
      val parse: Date = formatter.parse(args(0))
      println(parse.getTime);
  }
}
ToEpoch.main(args)
