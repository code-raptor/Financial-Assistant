package me.belakd.smsreadertest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionGranted = askPermission()
        initButtons(permissionGranted)
    }

    private fun askPermission(): Boolean {
        if (!hasPermission(this, Manifest.permission.READ_SMS)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Jogosultság megtagadva! Kérlek engedélyezd a beállításokban", Toast.LENGTH_LONG).show()
                return false
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), PERMISSION_CODE)
            }
        }
        return true
    }

    private fun hasPermission(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getSmsMessages(uriString: String, selection: String) {
        val smsList = ArrayList<SmsData>()
        val cursor: Cursor = contentResolver.query(
                Uri.parse("content://sms/$uriString"),
                null,
                selection,
                null,
                "date DESC"
        )

        if (cursor.moveToFirst()) {
            val nameID = cursor.getColumnIndex("address")
            val messageID = cursor.getColumnIndex("body")
            val dateID = cursor.getColumnIndex("date")
            do {
                val dateString = cursor.getString(dateID)
                smsList.add(SmsData(cursor.getString(nameID), Date(dateString.toLong()), cursor.getString(messageID)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        setupAdapter(smsList, selection)
    }

    private fun setupAdapter(smsList: ArrayList<SmsData>, selection: String) {
        val adapter = ListAdapter(this, smsList, selection)
        sms_list_view.adapter = adapter
    }

    private fun initButtons(permissionGranted: Boolean) {
        read_inbox.setOnClickListener {
            if (permissionGranted) {
                getSmsMessages("inbox", READ)
            } else {
                Toast.makeText(this, "Engedélyezze az sms olvasás jogosultságot!", Toast.LENGTH_SHORT).show()
            }
        }
        unread_inbox.setOnClickListener {
            if (permissionGranted) {
                getSmsMessages("inbox", UNREAD)
            } else {
                //ezzel csak szeretném meg mutatni, hogy igy lehet az app beállításába ugrani
                goToAndroidCurrentAppSetting()
                Toast.makeText(this, "Engedélyezze az sms olvasás jogosultságot!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToAndroidCurrentAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 0)
    }

    companion object {
        private const val PERMISSION_CODE = 1
        const val READ = "read=1"
        const val UNREAD = "read=0"
    }
}
