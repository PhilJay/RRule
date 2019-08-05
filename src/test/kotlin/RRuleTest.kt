import com.philjay.RRule
import com.philjay.Weekday
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RRuleTest {

    private val ruleStrings = listOf(
        "RRULE:FREQ=DAILY;INTERVAL=1",
        "RRULE:FREQ=WEEKLY;INTERVAL=2",
        "RRULE:FREQ=MONTHLY;INTERVAL=3",
        "RRULE:FREQ=YEARLY;INTERVAL=4",
        "RRULE:FREQ=WEEKLY;INTERVAL=2;BYDAY=FR;WKST=SU",
        "RRULE:FREQ=WEEKLY;INTERVAL=1;BYDAY=FR,SU",
        "RRULE:FREQ=DAILY;INTERVAL=1;UNTIL=20201009T220000Z",
        "RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5",
        "RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=5;BYDAY=2FR",
        "RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
        "RRULE:FREQ=MONTHLY;INTERVAL=1;COUNT=4;BYMONTHDAY=10,11,15",
        "RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=2,6"
    )

    @Test
    fun testRRule() {

        for (ruleString in ruleStrings) {
            val rule = RRule(ruleString)
            assertEquals(ruleString, rule.toRFC2445String())
        }
    }

    @Test
    fun testWeekdayFromString() {

        assertEquals(Weekday.Sunday, Weekday.fromString("Sunday"))
        assertEquals(Weekday.Monday, Weekday.fromString("mo"))
        assertEquals(Weekday.Thursday, Weekday.fromString("th"))
        assertEquals(Weekday.Friday, Weekday.fromString("FRIDAY"))
        assertEquals(Weekday.Wednesday, Weekday.fromString("wednesday"))
        assertNull(Weekday.fromString(""))
        assertNull(Weekday.fromString(null))
        assertNull(Weekday.fromString("s"))
    }
}