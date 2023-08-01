package com.example.thecodingstory.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.thecodingstory.R

class PasswordEditText: AppCompatEditText {

    private lateinit var warningImage: Drawable
    private lateinit var warningText: String

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }
    private fun init(){

        warningImage = ContextCompat.getDrawable(context, R.drawable.baseline_warning_amber_24) as Drawable
        addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length < 8)  showWarning() else hideWarning()
            }
            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }
        })
    }

    private fun showWarningButton(){
        setButtonDrawables(
            endOfTheText = warningImage
        )
    }

    private fun hideWarningButton(){
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun showWarning(){
        showWarningButton()
        warningText  = "Password kurang dari 8 karakter"
        super.setError(warningText,warningImage)
    }
    private fun hideWarning(){
        hideWarningButton()
        warningText = ""
    }

}