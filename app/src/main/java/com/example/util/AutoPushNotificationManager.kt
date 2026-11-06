package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.AutoPushNotification

object AutoPushNotificationManager {
    private const val CHANNEL_ID = "winzo_auto_push_channel"
    private const val CHANNEL_NAME = "WinZO Auto Push Notifications"
    private const val CHANNEL_DESC = "Automatic push alerts with WinZO logo and personalized rewards for users"

    fun initChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    fun showSystemNotification(context: Context, push: AutoPushNotification) {
        initChannel(context)

        // Android 13+ permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                // Cannot post system notification without permission; in-app banner will display
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("PUSH_ACTION", push.actionType.name)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            push.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Decode the WinZO Logo as the notification large icon
        val largeIconBitmap = try {
            BitmapFactory.decodeResource(context.resources, R.drawable.winzo_logo_1793967443731)
        } catch (_: Exception) {
            null
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(largeIconBitmap)
            .setContentTitle("${push.appName} • ${push.title}")
            .setContentText("${push.message} (for ${push.targetUser})")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${push.message}\n\n👤 Recipient: ${push.targetUser}\n⚡ App: ${push.appName}")
                    .setSummaryText(push.tag)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                push.actionLabel,
                pendingIntent
            )
            .build()

        try {
            NotificationManagerCompat.from(context).notify(push.id.hashCode(), notification)
        } catch (_: SecurityException) {
            // Permission rejected gracefully
        }
    }
}
