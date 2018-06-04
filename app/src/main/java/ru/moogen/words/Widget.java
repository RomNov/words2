package ru.moogen.words;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.RemoteViews;

import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Widget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        DataHelper dataHelper = new DataHelper(context);
        SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();

        Date today = new Date();
        String strToday = Word.getDateFormat().format(today);
        Word word = dataHelper.getWord(strToday, sqLiteDatabase);


        String name = word.getName();
        String description = word.getDescription();
        for (int i = 0; i < appWidgetIds.length; i++) {

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

            views.setTextViewText(R.id.text_view_widget_name, name);
            views.setTextViewText(R.id.text_view_widget_description, Html.fromHtml(description));


            AppWidgetManager.getInstance(context)
                    .updateAppWidget(new ComponentName(context, Widget.class), views);
        }

        Date todayDate = new Date();
        int hour = todayDate.getHours();
        if (hour >= 12 && hour <= 13){
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(name)
                            .setContentText(Html.fromHtml(description));

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
