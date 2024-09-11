package com.example.myapplication

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView

val ImageView.backgroundColor: Int
    get() = (this.background as ColorDrawable).color
