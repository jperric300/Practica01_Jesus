package com.example.practica01_jesus

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.practica01_jesus.data.User

@Suppress("DEPRECATION")
class PageHome : AppCompatActivity() {

    private lateinit var logout: AppCompatButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_home)

        val intent = intent
        val user: User? = intent.getSerializableExtra(USER_CORRECT_CREDENTIALS) as? User
        val userImageView = findViewById<ImageView>(R.id.userImageView)
        val nameUserView = findViewById<TextView>(R.id.nameUser)
        val emailUserView = findViewById<TextView>(R.id.emailUser)

        nameUserView.text = user?.name.orEmpty()
        emailUserView.text = user?.email.orEmpty()
        user?.imageUser?.let { userImageView.setImageResource(it) }

        logout = findViewById(R.id.logout)

        logout.setOnClickListener {

            exitSession()

        }
    }

    private fun exitSession() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar Sesión")
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")

        builder.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            finish()
        }

        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->

        }

        val dialog = builder.create()
        dialog.show()
    }


}






