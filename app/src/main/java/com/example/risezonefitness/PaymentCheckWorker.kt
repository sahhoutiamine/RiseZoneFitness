package com.example.risezonefitness


import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PaymentCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        listMembers.replaceAll { it.checkPaymentStatus() }

        addUnpaidMembers()

        checkMembersAndNotify()

        return Result.success()
    }
}
