package com.example.practica01_jesus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.mylog_inapplication.data.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {

    private lateinit var exitApp: ImageButton
    private lateinit var emailText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var login: AppCompatButton
    private var checkEmail: Boolean = false
    private var checkPass: Boolean = false
    private lateinit var passLayout: TextInputLayout

    //------------------------------------------------------
    //EXPRESIONES REGULARES
    //------------------------------------------------------
    private val reEmail = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    private val ncharacter = """^.{6,8}$""".toRegex()
    private val upperC = ".*[A-Z].*".toRegex()
    private val lowerC = ".*[a-z].*".toRegex()
    private val number = Regex(".*\\d+.*")


    private fun credentials(email: String, password: String): Boolean {

        data class User(val name: String, val email: String, val password: String)

        val userList = listOf(
            User("Jesus Miguel", "jesus@gmail.com", "123456Aa"),
            User("Admin", "admin@gmail.com", "123456Ab"),

            )

        val user = userList.find { it.email == email }
        return user?.password == password
    }

    private fun bindViews() {

        exitApp = findViewById(R.id.exitApp)
        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        login = findViewById(R.id.login)
        passLayout=findViewById(R.id.passwordInputLayout)

    }

    private fun functionCheckEmail(emailText: TextInputEditText, reEmail: Regex): Boolean {

        var result: Boolean = false;
        if (!emailText.text.toString().matches(reEmail)) {
            emailText.setError("no es un email válido")
        } else {
            emailText.setError(null)
            result = true
        }
        return result;
    }

    private fun checkData(checkPass: Boolean, checkEmail: Boolean): Boolean {
        var result: Boolean = false;
        result = checkEmail && checkPass
        return result;
    }

    private fun functionCheckPassword(
        passwordText: TextInputEditText, characterRankReg: Regex, upperC: Regex,
        lowerC: Regex, number: Regex
    ): Boolean {
        var result: Boolean = false;

        if (!passwordText.text.toString().matches(characterRankReg)) {
            passwordText.setError("la contraseña debe contener entre 6 y 8 dígitos")
        } else if (!passwordText.text.toString().matches(upperC)) {
            passwordText.setError("la contraseña debe contener como minimo una mayuscula")
        } else if (!passwordText.text.toString().matches(lowerC)) {
            passwordText.setError("la contraseña debe contener como minimo una minuscula")
        } else if (!passwordText.text.toString().matches(number)) {
            passwordText.setError("la contraseña debe contener como minimo un numero")
        } else {
            passwordText.setError(null)
            result = true;
        }
        return result;
    }

    private fun toastError(context: Context) {
        val errorMessage = "Credenciales incorrectas"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(context, errorMessage, duration)
        toast.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        exitApp.setOnClickListener {
            finishAffinity()
        }


        //------------------------------------------------------------------------------------------
        //Validar email
        //------------------------------------------------------------------------------------------

        emailText.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                checkEmail = functionCheckEmail(emailText, reEmail)
                checkPass = functionCheckPassword(passwordText, ncharacter, upperC, lowerC, number)
                login.isEnabled = checkData(checkPass, checkEmail)
                passwordText.requestFocus()
            }
            return@OnEditorActionListener true
            false
        })

        //------------------------------------------------------------------------------------------
        ////Validar contraseña
        //------------------------------------------------------------------------------------------

        passwordText.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                checkEmail = functionCheckEmail(emailText, reEmail)
                checkPass = functionCheckPassword(passwordText, ncharacter, upperC, lowerC, number)
                login.isEnabled = checkData(checkPass, checkEmail)
                login.requestFocus()

            }
            return@OnEditorActionListener true
            false
        })


        val userList = listOf(
            User("Jesus Miguel", "jesus@gmail.com", "123456Aa"),
            User("Admin", "admin@gmail.com", "123456Ab"),
        )

//        val userList = arrayListOf(
//            User("Jesus Miguel", "jesus@gmail.com", "123456Aa"),
//            User("Admin", "admin@gmail.com", "123456Ab"),
//        )


        login.setOnClickListener {

            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (credentials(email, password)) {
                login.visibility = View.INVISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    login.visibility = View.VISIBLE
                }, 1000)

                val intent = Intent(this, PageHome::class.java)
                // intent.putExtra("userList", userList)
                intent.putExtra(userList)
                startActivity(intent)

            } else {

                toastError(this)
            }

        }


    }
}

private fun Intent.putExtra(userList: List<User>) {

}






