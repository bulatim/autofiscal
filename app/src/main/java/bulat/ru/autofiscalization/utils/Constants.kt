package bulat.ru.autofiscalization.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

const val BASE_URL = "http://127.0.0.1:8080"
const val SERVICE_STATUS_BROADCAST_RECEIVER = "bulat.ru.autofiscalization.SERVICE_STATUS"
@SuppressLint("SimpleDateFormat")
val ddMMyyySimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")