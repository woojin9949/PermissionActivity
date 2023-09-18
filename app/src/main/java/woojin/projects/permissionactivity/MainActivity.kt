package woojin.projects.permissionactivity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import woojin.projects.permissionactivity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_RECORD_AUDIO_CODE = 200
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //녹음 시작하면 됨
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) -> {
                    showPermissonRationalDialog()
                }

                else -> {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        REQUEST_RECORD_AUDIO_CODE
                    )
                }
            }
        }
    }

    private fun showPermissonRationalDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜주셔야 앱을 정상적으로 사용하실 수 있습니다.")
            .setPositiveButton("권한 허용하기", DialogInterface.OnClickListener { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO_CODE
                )
            }).setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜주셔야 앱을 정상적으로 사용하실 수 있습니다.앱 설정 화면으로 진입하여 권한 허용")
            .setPositiveButton("권한 허용하러가기") { _, _ ->
                navigateToAppSetting()
            }.setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        if (audioRecordPermissionGranted) {
            //todo 녹음
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                showPermissonRationalDialog()
            } else {
                showPermissionSettingDialog()
            }
        }

    }


}