package com.movies.moviesapp.Ressources

import android.widget.RelativeLayout
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet

class SquareLayout : RelativeLayout {
    constructor(mContext: Context?) : super(mContext) {}
    constructor(mContext: Context?, attrs: AttributeSet?) : super(mContext, attrs) {}
    constructor(mContext: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        mContext,
        attrs,
        defStyleAttr
    ) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        mContext: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(mContext, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}