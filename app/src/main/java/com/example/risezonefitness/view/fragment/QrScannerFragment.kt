package com.example.risezonefitness.view.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.example.risezonefitness.R
import com.example.risezonefitness.view.activity.UserMainActivity
import com.example.risezonefitness.data.entryLogList
import com.example.risezonefitness.data.listMembers
import com.example.risezonefitness.model.AttendanceManager
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QrScannerFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var vibrator: Vibrator
    private var scanned = false
    private var username: String? = null

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? UserMainActivity)?.updateToolbarTitle("Scanner")

        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("RiseZonePrefs", Context.MODE_PRIVATE)
        barcodeView = view.findViewById(R.id.barcode_scanner)
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                barcodeView.decodeContinuous(callback)
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        checkCameraPermissionAndStart()
    }

    private fun checkCameraPermissionAndStart() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            barcodeView.decodeContinuous(callback)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    private val callback = object : BarcodeCallback {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun barcodeResult(result: BarcodeResult) {
            if (scanned) return
            scanned = true

            val qrContent = result.text.trim()
            username = arguments?.getString("username")
            val member = listMembers.find { it.username == username }

            member?.let {
                val actionText = when (qrContent) {
                    "RiseZoneGymEntry" -> {
                        if (!it.isInGym) {
                            it.isInGym = true
                            AttendanceManager.updateAttendanceIfNeeded(it)
                            playFeedback()
                            val log = "${it.fullName} Entry GYM  - ${getCurrentTime()}"
                            entryLogList.add(log)
                            log
                        } else {
                            Toast.makeText(context, "You are already in the gym.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    "RiseZoneGymExit" -> {
                        if (it.isInGym) {
                            it.isInGym = false
                            playFeedback()
                            val log = "${it.fullName} Exit GYM - ${getCurrentTime()}"
                            entryLogList.add(log)
                            log
                        } else {
                            Toast.makeText(context, "You are not in the gym.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    else -> Toast.makeText(context, "Invalid QR code", Toast.LENGTH_SHORT).show()
                }

                if (true) {
                    showResultDialog(qrContent, it.fullName)
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({ scanned = false }, 3000)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playFeedback() {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault())
        return format.format(Date())
    }

    private fun showResultDialog(status: String, name: String) {
        val username = arguments?.getString("username")
        val member = listMembers.find { it.username == username }
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_qr_result, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val textResult = dialogView.findViewById<TextView>(R.id.textResult)
        val btnClose = dialogView.findViewById<TextView>(R.id.txtClose)

        textResult.text = when {
            status == "RiseZoneGymEntry" && member?.isInGym == true -> {
                "Today's session is the opportunity to be stronger than yesterday! Ready for the challenge Mr $name?"
            }
            status == "RiseZoneGymExit" && member?.isInGym == false -> {
                "Step by step, we’re getting closer to your goals. Don’t forget to come back for your next session Mr $name!"
            }
            else -> {
                "Invalid QR code"
            }
        }


        btnClose.setOnClickListener {
            dialog.dismiss()
        }


        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}