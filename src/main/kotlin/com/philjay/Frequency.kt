package com.philjay


enum class Frequency {
    Daily, Weekly, Monthly, Yearly;

    override fun toString(): String {
        return super.toString().toUpperCase()
    }

    companion object {

        val values by lazy {
            values()
        }
    }
}