package com.manta.worldcup.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * viewPager에서 현재 프래그먼트의 위치를 나타내주는 인디케이터
 */
class CircleIndicator: LinearLayout {

    private var mContext: Context? = null

    private var mDefaultCircle: Int = 0
    private var mSelectCircle: Int = 0

    private var imageDot: MutableList<ImageView> = mutableListOf()

    //점 사이의 간격(dp)
    private val SPACE_BTW_DOT = 4.5f;

    //dp를 실제 화면에 보여질 픽셀크기로 바꾼다.
    private val padding = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, SPACE_BTW_DOT, resources.displayMetrics)

    constructor(context: Context) : super(context) {

        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        mContext = context
    }

    /**
     * 기본 점 생성
     * @param count 점의 갯수
     * @param defaultCircle 기본 점의 이미지
     * @param selectCircle 선택된 점의 이미지
     * @param position 선택된 점의 포지션
     */
    fun createDotPanel(count: Int, defaultCircle: Int, selectCircle: Int, position: Int) {

        this.removeAllViews()

        mDefaultCircle = defaultCircle
        mSelectCircle = selectCircle

        for (i in 0 until count) {
            imageDot.add(ImageView(mContext).apply { setPadding(padding.toInt(), 0, padding.toInt(), 0) })
            this.addView(imageDot[i])
        }

        //인덱스 선택
        selectDot(position)
    }

    /**
     * 선택된 점 표시
     * @param position
     */
    fun selectDot(position: Int) {
        for (i in imageDot.indices) {
            if (i == position)
                imageDot[i].setImageResource(mSelectCircle)
             else
                imageDot[i].setImageResource(mDefaultCircle)
        }

    }
}