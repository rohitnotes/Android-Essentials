// Juan Pablo Anaya
// MDF3 - 201608
// VoteHistoryWidget

package com.fullsail.android.anayajuan_ce10.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.fullsail.android.anayajuan_ce10.R;
import com.fullsail.android.anayajuan_ce10.VoteInfoActivity;

public class VoteHistoryWidget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {

            Intent intent = new Intent(context, ListWidgetService.class);
            intent.setData(Uri.fromParts("content", String.valueOf(widgetId), null)); // Leave this line alone.
            intent.putExtra(ListWidgetService.EXTRA_TYPE, ListWidgetService.TYPE_VOTE_HISTORY);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_widget_layout);
            rv.setRemoteAdapter(R.id.list, intent);
            rv.setEmptyView(R.id.list, R.id.empty);

            // TODO: Verify PendingIntent is valid.
            Intent voteInfo = new Intent(context, VoteInfoActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, voteInfo, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.list, pIntent);

            appWidgetManager.updateAppWidget(widgetId, rv);
        }
	}
}