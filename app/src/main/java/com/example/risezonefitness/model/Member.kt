package com.example.risezonefitness.model

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Member(
    var fullName: String = "",
    var age: Int = 0,
    var gender: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var cin: String = "",
    var username: String = "",
    var password: String = "",
    @get:Exclude var imageResource: Bitmap? = null,
    var isPaid: Boolean = true,
    var isInGym: Boolean = false,
    var registrationDate: Long = System.currentTimeMillis(),
    var attendanceThisWeek: Int = 0,
    var lastAttendanceReset: Long = System.currentTimeMillis()
)  {

    constructor() : this(
        "", 0, "", "", "", "", "", "", null, true, false,
        System.currentTimeMillis(), 0, System.currentTimeMillis()
    )


}
