package com.example.exampleapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.exampleapp.ui.theme.ExampleAppTheme

class ScoreBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.scoreboard)

        val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

        val countOfTen = findViewById<TextView>(R.id.countOfTen)
        val count = findViewById<TextView>(R.id.count)
        val reset = findViewById<Button>(R.id.clearbutton)

        var current = sharedPreferences.getInt("count_of_clicks", 0)
        count.text = "$current"

        var currentTens = sharedPreferences.getInt("count_of_tens", 0)
        countOfTen.text = "$currentTens"

        reset.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.putInt("count_of_clicks", 0)
            editor.putInt("count_of_tens", 0)
            editor.apply()

            countOfTen.text = "0"
            count.text = "0"
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ExampleAppTheme {
        Greeting2("Android")
    }
}