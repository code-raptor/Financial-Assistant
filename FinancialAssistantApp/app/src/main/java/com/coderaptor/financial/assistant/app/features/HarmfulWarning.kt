package com.coderaptor.financial.assistant.app.features

import android.util.Log
import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun harmfulChecker(productCategoryId: Long, dbHandler: DatabaseHandler) {
    if(dbHandler.isHarmfulCategory(productCategoryId)) {
        Log.i("harmful", "CategoryID: $productCategoryId is Harmful")
    }
}