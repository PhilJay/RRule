package com.philjay

data class WeekdayNum(
    val number: Int, // -53 - +53
    val weekday: Weekday
) {

    protected constructor() : this(0, Weekday.Monday)

    fun toICalString(): String {
        return if (number != 0)
            "$number" + weekday.initials
        else
            weekday.initials
    }
}