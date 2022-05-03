package ru.geekbrains.kotlin_lesson1.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_ACTIVITY_MESSAGE
import ru.geekbrains.kotlin_lesson1.utlis.KEY_BUNDLE_SERVICE_MESSAGE
import ru.geekbrains.kotlin_lesson1.utlis.KEY_WAVE
import java.lang.Thread.sleep

class MainService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work MainService")
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_ACTIVITY_MESSAGE)
            Log.d("@@@", "work MainService $extra")
            sleep(1000L)
            val message = Intent(KEY_WAVE)
            message.putExtra(
                KEY_BUNDLE_SERVICE_MESSAGE,
                "привет активити, и тебе всего хорошего"
            )
            sendBroadcast(message)
            //LocalBroadcastManager.getInstance(this).sendBroadcast(message)
        }
    }
}