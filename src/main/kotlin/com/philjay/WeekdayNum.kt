package com.philjay

data class WeekdayNum(
    val number: Int, // -53 - +53
    val weekday: Weekday
) {

    fun toICalString(): String {
        return if (number != 0)
            "$number" + weekday.initials
        else
            weekday.initials
    }
}