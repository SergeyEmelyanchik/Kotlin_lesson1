package ru.geekbrains.kotlin_lesson1.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_SERVICE_MESSAGE

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_SERVICE_MESSAGE) // TODO HW проблема с key2
            Log.d("@@@","MyBroadcastReceiver onReceive $extra")
        }
    }
}