package com.example.risezonefitness.view.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.risezonefitness.R
import com.example.risezonefitness.viewmodel.QrScannerViewModel
import com.example.risezonefitness.utils.AttendanceManager
import com.example.risezonefitness.view.activity.UserMainActivity
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.text.SimpleDateFormat
import java.util.*

class QrScannerFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferences
    private var scanned = false
    private var username: String? = null

    private val viewModel: QrScannerViewModel by viewModels()
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

        username = arguments?.getString("username")
        username?.let {
            viewModel.listenToMember(it)
        }

        observeViewModel()
    }

    private fun checkCameraPermissionAndStart() {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            barcodeView.decodeContinuous(callback)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.errorState.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private val callback = object : BarcodeCallback {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun barcodeResult(result: BarcodeResult) {
            if (scanned) return
            scanned = true

            val qrContent = result.text.trim()

            val member = viewModel.memberState.value
            member?.let {
                when (qrContent) {
                    "RiseZoneGymEntry" -> {
                        if (!it.isInGym) {
                            viewModel.updateMemberInGymStatus(it.username, true)
                            AttendanceManager.updateAttendanceIfNeeded(it,
                                onSuccess = { playFeedback() },
                                onFailure = { showToast(it) }
                            )
                            viewModel.saveAttendanceLog(it.fullName, true)
                            showResultDialog(qrContent, it.fullName)
                            playFeedback()
                        } else {
                            showToast("You are already in the gym.")
                        }
                    }

                    "RiseZoneGymExit" -> {
                        if (it.isInGym) {
                            viewModel.updateMemberInGymStatus(it.username, false)
                            playFeedback()
                            showResultDialog(qrContent, it.fullName)
                            viewModel.saveAttendanceLog(it.fullName, false)
                        } else {
                            showToast("You are not in the gym.")
                        }
                    }

                    else -> {
                        showToast("Invalid QR code")
                    }
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({ scanned = false }, 3000)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playFeedback() {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentTime(): String {
        val format = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault())
        return format.format(Date())
    }

    private fun showResultDialog(status: String, name: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_qr_result, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val textResult = dialogView.findViewById<TextView>(R.id.textResult)
        val btnClose = dialogView.findViewById<TextView>(R.id.txtClose)

        val message = when (status) {
            "RiseZoneGymEntry" -> "Today's session is the opportunity to be stronger than yesterday! Ready for the challenge Mr $name?"
            "RiseZoneGymExit" -> "Step by step, we’re getting closer to your goals. Don’t forget to come back for your next session Mr $name!"
            else -> "Invalid QR code"
        }

        textResult.text = message

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
