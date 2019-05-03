package com.coderaptor.financial.assistant.app.util.spinner

import com.coderaptor.financial.assistant.app.data.DatabaseHandler

fun getCategoryStringWithIdList(db: DatabaseHandler, onlyHarmful: Boolean = false): ArrayList<StringWithId> {
    val stringWithIdList = ArrayList<StringWithId>()
    val all = db.findAllProductCategory()
    for (item in all) {
        if (!onlyHarmful) {
            stringWithIdList.add(StringWithId(item.name, item.id))
        }else {
            if (!db.isHarmfulCategory(item.id)) {
                stringWithIdList.add(StringWithId(item.name, item.id))
            }
        }
    }
    return stringWithIdList
}