package com.fynzero.finalsubmission.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.fynzero.finalsubmission.helper.MappingHelper
import com.fynzero.finalsubmission.model.User
import java.util.concurrent.ExecutionException

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null
    private val items = ArrayList<User>()

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val list = MappingHelper.mapCursorToArrayList(cursor)
        items.addAll(list)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onCreate() {}

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        try {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(items[position].avatar_url)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.imageView, bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val extras = bundleOf(UserFavoriteWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = items.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

}