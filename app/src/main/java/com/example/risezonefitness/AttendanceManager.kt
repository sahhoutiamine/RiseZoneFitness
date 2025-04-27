package com.example.risezonefitness



import com.example.risezonefitness.model.Member
import java.util.Calendar
import kotlin.apply
import kotlin.collections.set

object AttendanceManager {


    fun updateAttendanceIfNeeded(member: Member) {
        val now = Calendar.getInstance()

        if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) return

        val lastReset = Calendar.getInstance().apply {
            timeInMillis = member.lastAttendanceReset
        }

        val nowWeek = now.get(Calendar.WEEK_OF_YEAR)
        val nowYear = now.get(Calendar.YEAR)
        val lastWeek = lastReset.get(Calendar.WEEK_OF_YEAR)
        val lastYear = lastReset.get(Calendar.YEAR)

        if (nowWeek != lastWeek || nowYear != lastYear) {
            member.attendanceThisWeek = 0
            member.lastAttendanceReset = System.currentTimeMillis()
            memberAttendanceDays[member.username] = mutableSetOf()
        }

        val key = "${nowYear}-${nowWeek}-${now.get(Calendar.DAY_OF_WEEK)}"
        if (!memberAttendanceDays.containsKey(member.username)) {
            memberAttendanceDays[member.username] = mutableSetOf()
        }

        if (!memberAttendanceDays[member.username]!!.contains(key)) {
            memberAttendanceDays[member.username]!!.add(key)
            member.attendanceThisWeek++
        }
    }
}
