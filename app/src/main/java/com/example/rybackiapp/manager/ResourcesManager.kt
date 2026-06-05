package com.example.rybackiapp.manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes id: Int): String {
        return context.resources.getString(id)
    }

    fun getString(
        @StringRes id: Int,
        @SuppressLint("SupportAnnotationUsage") @StringRes vararg s: String,

        ): String {
        return context.resources.getString(id, *s)
    }

    fun getImage(
        @DrawableRes id: Int,
    ): Painter {
        val drawable = context.resources.getDrawable(id)
        return BitmapPainter(drawable.toBitmap().asImageBitmap())
    }

}