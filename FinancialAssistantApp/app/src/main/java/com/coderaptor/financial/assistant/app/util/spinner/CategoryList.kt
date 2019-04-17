package com.coderaptor.financial.assistant.app.util.spinner

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun getCategoryStringWithIdList(db: DatabaseHandler): ArrayList<StringWithId> {
    val stringWithIdList = ArrayList<StringWithId>()
    val all = db.findAllProductCategory()
    for (item in all) {
        stringWithIdList.add(StringWithId(item.name, item.id))
    }
    return stringWithIdList
}