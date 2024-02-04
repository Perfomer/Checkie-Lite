package com.perfomer.checkielite.common.android.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

/**
 * https://stackoverflow.com/a/9989900
 */
fun Uri.getRealPath(context: Context): String {
	val result: String
	val cursor = context.contentResolver?.query(this, null, null, null, null)

	if (cursor == null) {
		result = path.toString()
	} else {
		cursor.moveToFirst()
		val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
		result = cursor.getString(idx)
		cursor.close()
	}

	return result
}