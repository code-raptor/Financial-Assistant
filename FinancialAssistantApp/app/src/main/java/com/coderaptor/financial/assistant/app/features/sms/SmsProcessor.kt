package com.coderaptor.financial.assistant.app.features.sms

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun askPermission(context: Context, activity: Activity): Boolean {
    if (!hasPermission(context, Manifest.permission.READ_SMS)) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)) {
            Toast.makeText(context, "Jogosultság megtagadva! Kérlek engedélyezd a beállításokban", Toast.LENGTH_LONG).show()
            return false
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_SMS), PERMISSION_CODE)
            Toast.makeText(context, "Jogosultság megtagadva! Kérlek engedélyezd a beállításokban", Toast.LENGTH_LONG).show()
            return false
        }
    }else {
        return true
    }
}

private fun hasPermission(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getSmsMessages(context: Context, readSmsId: Long, dbHandler: DatabaseHandler
): Pair<Long, Int> {
    val smsList = ArrayList<SmsData>()
    val cursor: Cursor = context.contentResolver.query(
        Uri.parse("content://sms/inbox"),
        null,
        SELECTION,
        null,
        "date DESC"
    )
    var result: Pair<Long, Int> = (-1).toLong() to -1
    if (cursor.moveToFirst()) {
        val idColumn = cursor.getColumnIndex("_id")
        val addressColumn = cursor.getColumnIndex("address")
        val messageColumn = cursor.getColumnIndex("body")
        do {
            val sms = SmsData(cursor.getLong(idColumn),cursor.getString(addressColumn), cursor.getString(messageColumn))
            smsList.add(sms)
        } while (cursor.moveToNext())
    }

    val rowCount = cursor.count
    cursor.close()
    if (rowCount < readSmsId) {
        dbHandler.deleteTableContent(DatabaseHandler.TABLE_NAME_SMS)
    }
    if (smsList.isNotEmpty()) {
        if (smsList.size == 1) {
            val amount = findAmount(smsList[0].message)
            result = smsList[0].id to amount
        }else {
            for(sms in smsList) {
                if (sms.id >= readSmsId) {
                    val amount = findAmount(smsList[0].message)
                    result = sms.id to amount
                }
            }
        }
    }
    return result
}

const val SELECTION = "read=0 AND body LIKE 'K&H%' AND body LIKE '%HUF%'"
const val PERMISSION_CODE = 1