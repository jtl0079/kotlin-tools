package com.myorg.kotlintools.firebase

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant


fun showDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("确定", null)
        .show()
}

fun defaultFun(
    context: Context
) {
    val db = FirebaseFirestore.getInstance()
    val data = hashMapOf(
        "name" to "John",
        "age" to 22,
        "create time" to Instant.now()
    )

    db.collection("testing")
        .add(data)
        .addOnSuccessListener {
            Log.d("FS", "写入成功: ${it.id}")
            showDialog(context, "成功 ✅", "数据已写入 Firestore")
        }
        .addOnFailureListener { e ->
            Log.e("FS", "写入失败", e)
            showDialog(context, "失败 ❌", e.message ?: "未知错误")
        }
}

@Composable
fun DefaultFirebaseFirestoreButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            defaultFun(context)
        }
    ) { }
}