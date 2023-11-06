package com.aarush.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aarush.newsapp.databinding.ActivityLoginFormBinding
import com.aarush.newsapp.ui.main.MainActivity

class login_form : AppCompatActivity() {
    private lateinit var bind: ActivityLoginFormBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val dbhelp = DB_class(applicationContext)
        val db = dbhelp.readableDatabase

        bind.btnlogin.setOnClickListener {
            val username = bind.logtxt.text.toString()
            val password = bind.ed3.text.toString()
            val query = "SELECT * FROM user WHERE username='$username' AND pswd='$password'"
            val rs = db.rawQuery(query, null)
            if (rs.moveToFirst()) {
                val name = rs.getString(rs.getColumnIndex("name"))
                rs.close()
                startActivity(Intent(this, MainActivity::class.java).putExtra("name", name))
            } else {
                val ad = AlertDialog.Builder(this)
                ad.setTitle("Message")
                ad.setMessage("Username or password is incorrect!")
                ad.setPositiveButton("Ok", null)
                ad.show()
            }
        }

        bind.regisLink.setOnClickListener {
            val intent = Intent(this, signupForm::class.java)
            startActivity(intent)
        }

        // Toggle password visibility
        bind.passwordVisibilityButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                bind.ed3.inputType = 129 // Show Password
            } else {
                bind.ed3.inputType = 128 // Hide Password
            }
            bind.ed3.setSelection(bind.ed3.text.length)
        }
    }
}
