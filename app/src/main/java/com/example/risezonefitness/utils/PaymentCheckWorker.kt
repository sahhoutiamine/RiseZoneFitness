package com.example.risezonefitness.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.risezonefitness.model.addUnpaidMembers
import com.example.risezonefitness.model.checkMembersAndNotify
import com.example.risezonefitness.data.listMembers

class PaymentCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        listMembers.replaceAll { it.checkPaymentStatus() }

        addUnpaidMembers()

        checkMembersAndNotify()

        return Result.success()
    }
}