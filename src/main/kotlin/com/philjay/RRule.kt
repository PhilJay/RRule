package com.philjay

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

open class RRule() {

    private val name = "RRULE"

    var freq: Frequency = Frequency.Daily

    var wkst: Weekday? = null
    var until: Instant? = null
    var count = 0
    var interval = 0

    val byDay = arrayListOf<WeekdayNum>()
    val byMonth = arrayListOf<Int>() // in +/-[1-12]
    val byMonthDay = arrayListOf<Int>()  // in +/-[1-31]
    val byWeekNo = arrayListOf<Int>()  // in +/-[1-53]
    val byYearDay = arrayListOf<Int>()  // in +/-[1-366]
    val bySetPos = arrayListOf<Int>()  // in +/-[1-366]

    constructor(rfc5545String: String) : this() {
        val components = rfc5545String.replace("$name:", "").split(";", "=")
        var i = 0
        while (i < components.size) {
            val component = components[i]
            if (component == "FREQ") {
                i += 1
                freq = when (components[i]) {
                    "DAILY" -> Frequency.Daily
                    "WEEKLY" -> Frequency.Weekly
                    "MONTHLY" -> Frequency.Monthly
                    "YEARLY" -> Frequency.Yearly
                    else -> Frequency.Daily
                }
            }
            if (component == "INTERVAL") {
                i += 1
                interval = components[i].toIntOrNull() ?: 1
            }
            if (component == "BYDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val weekDay = weekDayFromString(dayString)

                    if (weekDay != null) {
                        if (dayString.length > 2) {
                            val number = dayString.replace(Regex("[^-?0-9]+"), "").toIntOrNull() ?: 0
                            byDay.add(WeekdayNum(number, weekDay))
                        } else {
                            byDay.add(WeekdayNum(0, weekDay))
                        }
                    }
                }
            }

            if (component == "BYMONTHDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val monthDay = dayString.toIntOrNull()
                    if (monthDay != null) {
                        byMonthDay.add(monthDay)
                    }
                }
            }

            if (component == "BYMONTH") {
                i += 1
                val monthStrings = components[i].split(",")
                for (monthString in monthStrings) {
                    val month = monthString.toIntOrNull()
                    if (month != null) {
                        byMonth.add(month)
                    }
                }
            }

            if (component == "BYWEEKNO") {
                i += 1
                val weekStrings = components[i].split(",")
                for (weekString in weekStrings) {
                    val week = weekString.toIntOrNull()
                    if (week != null) {
                        byWeekNo.add(week)
                    }
                }
            }

            if (component == "BYYEARDAY") {
                i += 1
                val dayStrings = components[i].split(",")
                for (dayString in dayStrings) {
                    val yearDay = dayString.toIntOrNull()
                    if (yearDay != null) {
                        byYearDay.add(yearDay)
                    }
                }
            }

            if (component == "BYSETPOS") {
                i += 1
                val posStrings = components[i].split(",")
                for (posString in posStrings) {
                    val pos = posString.toIntOrNull()
                    if (pos != null) {
                        bySetPos.add(pos)
                    }
                }
            }

            if (component == "COUNT") {
                i += 1
                count = components[i].toIntOrNull() ?: 1
            } else if (component == "UNTIL") {
                i += 1
                until = LocalDateTime.parse(components[i], dateFormatter).toInstant(ZoneOffset.UTC)
            }

            if (component == "WKST") {
                i += 1
                wkst = weekDayFromString(components[i])
            }
            i++
        }
    }

    /**
     * Transforms this RRule to a RFC5545 standard iCal String.
     */
    fun toRFC5545String(): String {

        val buf = StringBuilder()
        buf.append("$name:")
        buf.append("FREQ=").append(freq.toString())
        if (interval > 0) {
            buf.append(";INTERVAL=").append(interval)
        }
        if (until != null) {
            buf.append(";UNTIL=").append(dateFormatter.format(until))
        }
        if (count > 0) {
            buf.append(";COUNT=").append(count)
        }
        if (byYearDay.isNotEmpty()) {
            buf.append(";BYYEARDAY=")
            writeIntList(byYearDay, buf)
        }
        if (byMonth.isNotEmpty()) {
            buf.append(";BYMONTH=")
            writeIntList(byMonth, buf)
        }
        if (byMonthDay.isNotEmpty()) {
            buf.append(";BYMONTHDAY=")
            writeIntList(byMonthDay, buf)
        }
        if (byWeekNo.isNotEmpty()) {
            buf.append(";BYWEEKNO=")
            writeIntList(byWeekNo, buf)
        }
        if (byDay.isNotEmpty()) {
            buf.append(";BYDAY=")
            var first = true
            for (day in byDay) {
                if (!first) {
                    buf.append(',')
                } else {
                    first = false
                }
                buf.append(day.toICalString())
            }
        }
        if (bySetPos.isNotEmpty()) {
            buf.append(";BYSETPOS=")
            writeIntList(bySetPos, buf)
        }
        if (wkst != null) {
            buf.append(";WKST=").append(wkst?.initials)
        }

        return buf.toString()
    }

    private fun writeIntList(integers: List<Int>, out: StringBuilder) {
        for (i in integers.indices) {
            if (0 != i) {
                out.append(',')
            }
            out.append(integers[i])
        }
    }

    private fun weekDayFromString(dayString: String): Weekday? {
        return when {
            dayString.contains("SU") -> Weekday.Sunday
            dayString.contains("MO") -> Weekday.Monday
            dayString.contains("TU") -> Weekday.Tuesday
            dayString.contains("WE") -> Weekday.Wednesday
            dayString.contains("TH") -> Weekday.Thursday
            dayString.contains("FR") -> Weekday.Friday
            dayString.contains("SA") -> Weekday.Saturday
            else -> null
        }
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC)
    }
}