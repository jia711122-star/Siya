
package com.siya.assistant

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var speech: TextToSpeech
    private lateinit var message: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speech = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                speech.language = Locale("hi", "IN")
            }
        }

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)
        layout.setBackgroundColor(Color.rgb(15, 15, 25))

        val title = TextView(this)
        title.text = "SIYA"
        title.textSize = 36f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val welcome = TextView(this)
        welcome.text = "Hello! Main Siya hoon ❤️"
        welcome.textSize = 20f
        welcome.setTextColor(Color.WHITE)
        welcome.gravity = Gravity.CENTER

        message = EditText(this)
        message.hint = "Mujhse kuch bhi bolo..."
        message.setTextColor(Color.WHITE)
        message.setHintTextColor(Color.LTGRAY)
        message.setSingleLine(true)

        val mic = Button(this)
        mic.text = "🎤 Bolo Siya"

        val send = Button(this)
        send.text = "Send"

        result = TextView(this)
        result.text = "Main tumhari assistant hoon."
        result.textSize = 18f
        result.setTextColor(Color.WHITE)
        result.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(welcome)
        layout.addView(message)
        layout.addView(mic)
        layout.addView(send)
        layout.addView(result)

        setContentView(layout)

        mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "hi-IN"
            )
            try {
                startActivityForResult(intent, 100)
            } catch (e: Exception) {
                result.text = "Voice service available nahi hai."
            }
        }

        send.setOnClickListener {
            val text = message.text.toString()
            result.text = "Tumne kaha: $text"
            if (text.isNotBlank()) {
                speech.speak(
                    "Tumne kaha: $text",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "siya_reply"
                )
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val words = data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )
            if (!words.isNullOrEmpty()) {
                message.setText(words[0])
            }
        }
    }

    override fun onDestroy() {
        if (::speech.isInitialized) {
            speech.stop()
            speech.shutdown()
        }
        super.onDestroy()
    }
}
