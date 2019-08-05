# RRule
Kotlin implementation for handling iCalendar (RFC 5545) recurrence rules 

## Dependency



## Sample Usage

Transform iCalendar RFC 5545 String to RRule object:

```kotlin
val rrule = RRule("RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU")
```

Transform RRule object to iCalendar RFC 5545 String:

```kotlin
val rfc5545String = rrule.toRFC2445String()
```
