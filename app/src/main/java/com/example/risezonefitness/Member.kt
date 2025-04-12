package com.example.risezonefitness


data class Member(
    val name: String,
    val age: Int,
    val gender: String,
    val isInGym: Boolean,
    val attendanceDays: Int,
    val isPaid: Boolean,
    val imageResource: Int
)