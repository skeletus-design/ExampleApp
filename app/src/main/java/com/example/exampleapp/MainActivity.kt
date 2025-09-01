package com.example.exampleapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exampleapp.ui.theme.ExampleAppTheme

var count = 0

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Android 13+ — проверка и запрос разрешения
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001 // requestCode
                )
            }
        }

        //Кнопка инициализируется через метод findViewById
        val Button = findViewById<Button>(R.id.button)
        val Text = findViewById<TextView>(R.id.textView)
        var current = sharedPreferences.getInt("count_of_clicks", 0)
        var currentTen = sharedPreferences.getInt("count_of_tens", 0)
        Text.text = "0"

        //Кнопка перехода к счету
        val ScoreButton = findViewById<Button>(R.id.main)

        //RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)

        //Данные для RecyclerView
        val dataList = listOf(
            "Сегодня отличная погода!",
            "RecyclerView удобен для больших списков.",
            "Kotlin — современный и лаконичный язык.",
            "Android Studio упрощает разработку приложений.",
            "Material Design делает интерфейсы красивыми.",
            "Жизнь — это путешествие, а не пункт назначения.",
            "Учиться программированию — это весело!",
            "Кофе — лучший друг разработчика.",
            "Лучший способ предсказать будущее — создать его.",
            "Не бойтесь совершать ошибки, они учат нас.",
            "Прогресс — это маленькие шаги каждый день.",
            "Код должен быть читаемым и понятным.",
            "Вдохновение приходит во время работы.",
            "Тестирование — важная часть разработки.",
            "Оптимизация кода улучшает производительность.",
            "Документируйте свой код для будущего себя.",
            "Гит — мощный инструмент для контроля версий.",
            "Компьютеры делают только то, что вы им говорите.",
            "Алгоритмы — это основа программирования.",
            "Отладка превращает ошибки в опыт."
        )

        //Adapter
        val adapter = MyAdapter(dataList) // Создаем адаптер и передаем данные

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(dataList)

        //setOnClickListener
        Button.setOnClickListener {
            //Тут объявляются действия, когда кнопка нажата.
            count = count + 1

            var score = current + count
            editor.putInt("count_of_clicks", score)


            Text.text = "$count"
            if (Text.text == "10") {
                //Toast.makeText(this, "Число достигло десяти!", Toast.LENGTH_SHORT).show()
                var scoreTen = currentTen + 1
                editor.putInt("count_of_tens", scoreTen)
                editor.apply()
                showSimpleDialog(this)
            }

            editor.apply()
        }

        //showScoreListener
        ScoreButton.setOnClickListener {
            startActivity(Intent(this, ScoreBoard::class.java))
        }
    }

    //Вызов Dialog
    fun showSimpleDialog(context: Context) {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_layout)

        //Обновление счетчика
        val Text = findViewById<TextView>(R.id.textView)
        Text.text = "0"
        count = 0


        // Настройка элементов диалога
        val title = dialog.findViewById<TextView>(R.id.dialog_title)
        val message = dialog.findViewById<TextView>(R.id.dialog_message)
        val okButton = dialog.findViewById<Button>(R.id.ok_button)

        // Отображаемый контент
        title.text = "Мой диалог"
        message.text = "Пу-пу-пуу..."

        okButton.setOnClickListener {
            // Действие при нажатии кнопки
            sendNotification()
            dialog.dismiss() // закрываем диалог
        }

        // Настройка поведения диалога
        dialog.setCancelable(true) // можно ли закрыть диалог кнопкой назад
        // Настройка для прозрачного фона.
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(), // 85% ширины экрана
            WindowManager.LayoutParams.WRAP_CONTENT)
    }

    @SuppressLint("MissingPermission")
    fun sendNotification() {
        val channelId = "208654"
        val channelName = "My Notifications"
        val channelDescription = "Notifications for my app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        // Создаём канал (только для Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Intent для клика по уведомлению
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Строим уведомление
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.kalculate_icon)
            .setContentTitle("My Notification Title")
            .setContentText("This is the notification message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationId = 208654

        // Разрешение есть или Android < 13
        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExampleAppTheme {
        Greeting("Android")
    }
}