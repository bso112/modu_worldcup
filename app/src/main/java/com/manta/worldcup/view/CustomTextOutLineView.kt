package com.manta.worldcup.view
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.manta.worldcup.R


@SuppressLint("AppCompatCustomView")
class CustomTextOutLineView : TextView {

    private var stroke = false
    private var strokeWidth = 0.0f
    private var strokeColor = 0

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {

        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initView(context, attrs)
    }

    constructor(context: Context?) : super(context) {

    }


    private fun initView(context: Context, attrs: AttributeSet?) {

        //xml 파일에서 설정된 textoutline: 관련 설정들을 가져온다.
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomTextOutLineView)
        //마치 cpp랑 쉐이더와의 통신같은 느낌.
        stroke = attr.getBoolean(R.styleable.CustomTextOutLineView_textStroke, false)
        strokeWidth = attr.getFloat(R.styleable.CustomTextOutLineView_textStrokeWidth, 0.0f)
        strokeColor = attr.getColor(R.styleable.CustomTextOutLineView_textStrokeColor, 0xffffff)

    }

    //렌더러에 의해 불리겠지.
    override fun onDraw(canvas: Canvas) {
        if (stroke) {
            val states = textColors
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            setTextColor(strokeColor)
            super.onDraw(canvas)
            paint.style = Paint.Style.FILL
            setTextColor(states)
        }
        super.onDraw(canvas)
    }
}