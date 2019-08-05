package com.philjay

enum class Weekday(val initials: String) {
    Monday("MO"), Tuesday("TU"), Wednesday("WE"), Thursday("TH"), Friday("FR"), Saturday("SA"), Sunday("SU");

    companion object {

        val values by lazy {
            values()
        }

        fun fromString(string: String?): Weekday? {
            return if (string.isNullOrEmpty() || string.length < 2) {
                null
            } else {
                try {
                    string.toLowerCase()
                    string[0].toUpperCase()
                    valueOf(string)
                } catch (e: Exception) {
                    val dayInitials = string.substring(0, 2)

                    for (value in values) {
                        if (value.initials.equals(dayInitials, true)) {
                            return value
                        }
                    }
                    null
                }
            }
        }
    }
}