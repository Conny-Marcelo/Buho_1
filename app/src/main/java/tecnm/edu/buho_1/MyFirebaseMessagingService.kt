package tecnm.edu.buho_1

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Obtén los datos de la notificación
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // Muestra la notificación en el sistema de notificación de Android
        // Aquí puedes personalizar la apariencia de la notificación según tus necesidades
        // Puedes agregar acciones, iconos, etc.
        // Aquí se muestra una notificación básica con título y cuerpo.
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.logo)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}