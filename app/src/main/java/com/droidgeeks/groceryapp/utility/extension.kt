package com.droidgeeks.groceryapp.utility

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

//Navigate from One activity to Another and Clear Previous activities
inline fun <reified T : Activity> Activity.navigate(
    finish: Boolean = true,
    anim: String = "",
    clear: Boolean = true
) {
    val intent = Intent(this, T::class.java)
    if (clear) {
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
    }
    startActivity(intent)
    if (finish) {
        finish()
    }

//    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun EditText.disableError(parent: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                if (parent != null) {
                    if (parent.isErrorEnabled) {
                        if (s.toString().isNotEmpty()) {
                            parent.isErrorEnabled = false
//                            parent.setBackgroundColor(Color.TRANSPARENT)
//                            setTextColor(Color.BLACK)
                        }
                    }
                }
            }
        }
    })
}