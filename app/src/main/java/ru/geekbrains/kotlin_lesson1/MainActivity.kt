package ru.geekbrains.kotlin_lesson1


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.TextView
import ru.geekbrains.kotlin_lesson1.repo.MyDataClass
import ru.geekbrains.kotlin_lesson1.repo.MyTestObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }

    fun init() {
        val myData = MyDataClass(1, "Минск")
        val mySecondData = myData.copy()
        mySecondData.town = "Минск"
        mySecondData.weather = 0

        val textView: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.btn1)
        button.setOnClickListener {
            textView.text = "Город: " + mySecondData.town + "\nТемпература: " + mySecondData.weather + " градусов!"
            textView.append("\n" + MyTestObject.getMyTest())
            MyTestObject.getTestForEach()
        }


    }


}