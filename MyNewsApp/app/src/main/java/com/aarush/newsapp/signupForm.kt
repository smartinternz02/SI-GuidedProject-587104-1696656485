package com.aarush.newsapp

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity;
import com.aarush.newsapp.databinding.ActivitySignupFormBinding;
class signupForm : AppCompatActivity() {
        private lateinit var binding: ActivitySignupFormBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySignupFormBinding.inflate(layoutInflater)
            setContentView(binding.root)
            var dbhelp = DB_class(applicationContext)
            var db = dbhelp.writableDatabase
            binding.btnrgs.setOnClickListener {
                var name = binding.ed1.text.toString()
                var username = binding.ed2.text.toString()
                var password = binding.ed3.text.toString()
                if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                    var data = ContentValues()
                    data.put("name", binding.ed1.text.toString())
                    data.put("username", binding.ed2.text.toString())
                    data.put("pswd", binding.ed3.text.toString())
                    var rs: Long = db.insert("user", null, data)
                    if (!rs.equals(-1)) {
                        var ad = AlertDialog.Builder(this)
                        ad.setTitle("Message")
                        ad.setMessage("Account registered successfully")
                        ad.setPositiveButton("Ok", null)
                        ad.show()
                        binding.ed1.text.clear()
                        binding.ed2.text.clear()
                        binding.ed3.text.clear()
                        val handler = Handler()
                        handler.postDelayed({
                            // Start the login_form activity after a 1.5-second delay
                            val intent = Intent(this, login_form::class.java)
                            startActivity(intent)
                        }, 1500)
                    } else {
                        var ad = AlertDialog.Builder(this)
                        ad.setTitle("Message")
                        ad.setMessage("Record not added")
                        ad.setPositiveButton("Ok", null)
                        ad.show()
                        binding.ed1.text.clear()
                        binding.ed2.text.clear()
                        binding.ed3.text.clear()
                    }
                } else {
                    Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                }
            }
            binding.loginLink.setOnClickListener {
                val intent = Intent(this, login_form::class.java)
                startActivity(intent)
            }
        }
    }
