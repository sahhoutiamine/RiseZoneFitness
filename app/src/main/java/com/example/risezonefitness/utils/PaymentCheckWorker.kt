package com.example.risezonefitness.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PaymentCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        MemberExtensions.checkAndUpdateMembersPaymentStatus()

        PaymentNotify.notifyUnpaidMembers()

        return Result.success()
    }
}