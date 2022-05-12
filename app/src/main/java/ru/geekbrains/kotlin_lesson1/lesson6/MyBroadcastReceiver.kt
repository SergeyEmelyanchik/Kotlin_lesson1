package ru.geekbrains.kotlin_lesson1.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.geekbrains.kotlin_lesson1.utlis.LOG_KEY
import ru.geekbrains.kotlin_lesson1.utlis.LOG_KEY_SECOND
import ru.geekbrains.kotlin_lesson1.utlis.MAIN_SERVICE_KEY

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_KEY_SECOND, "MyBroadcastReceiver onReceive ${intent.action}")
        intent.let {
            val stringExtra = it.getStringExtra(MAIN_SERVICE_KEY)
            Log.d(LOG_KEY, "MainService говорит через MyBroadcastReceiver: $stringExtra")
        }
    }
}
