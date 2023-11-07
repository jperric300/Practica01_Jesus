package com.example.practica01_jesus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.practica01_jesus.data.User
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


const val USER_CORRECT_CREDENTIALS = "user_correct_credentials"
private const val EMAIL_PREFS_KEY = "email"
private const val PASSWORD_PREFS_KEY = "password"

class MainActivity : AppCompatActivity() {

    private lateinit var exitApp: ImageButton
    private lateinit var emailText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var login: AppCompatButton
    private var checkEmail: Boolean = false
    private var checkPass: Boolean = false
    private lateinit var passLayout: TextInputLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchDate: SwitchMaterial

    private val reEmail = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    private val ncharacter = """^.{6,8}$""".toRegex()
    private val upperC = ".*[A-Z].*".toRegex()
    private val lowerC = ".*[a-z].*".toRegex()
    private val number = Regex(".*\\d+.*")



    private fun credentials(email: String, password: String): User? {

        val userList = listOf(
            User("Jesus Miguel", "jesus@gmail.com", "123456Aa", R.drawable.user_image_mayor),
            User("Admin", "admin@gmail.com", "123456Ab", R.drawable.user_image_administrator),
        )

        val user = userList.find { it.email == email }
        return user?.takeIf { it.password == password }
    }

    private fun bindViews() {

        exitApp = findViewById(R.id.exitApp)
        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        login = findViewById(R.id.login)
        passLayout = findViewById(R.id.passwordInputLayout)

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
        passwordText: TextInputEditText,
        characterRankReg: Regex,
        upperC: Regex,
        lowerC: Regex,
        number: Regex
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
            User("Jesus Miguel", "jesus@gmail.com", "123456Aa", R.drawable.user_image_mayor),
            User("Admin", "admin@gmail.com", "123456Ab", R.drawable.user_image_administrator),
        )

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        switchDate = findViewById(R.id.saveDate)

        switchDate.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("saveCredentials", isChecked)
            editor.apply()
        }

        val saveCredentials = sharedPreferences.getBoolean("saveCredentials", false)

        switchDate.isChecked = saveCredentials

        if (saveCredentials) {
            emailText.setText(sharedPreferences.getString(EMAIL_PREFS_KEY, ""))
            passwordText.setText(sharedPreferences.getString(PASSWORD_PREFS_KEY, ""))
            checkEmail = functionCheckEmail(emailText, reEmail)
            checkPass = functionCheckPassword(passwordText, ncharacter, upperC, lowerC, number)
            login.isEnabled = checkData(checkPass, checkEmail)
        }

        login.setOnClickListener {
            val EMAIL_PREFS = emailText.text.toString()
            val PASSWORD_PREFS = passwordText.text.toString()
            val user = credentials(EMAIL_PREFS, PASSWORD_PREFS)

            user?.let {
                login.visibility = View.INVISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    login.visibility = View.VISIBLE
                }, 1000)

                val intent = Intent(this, PageHome::class.java)


                if (switchDate.isChecked) {
                    val editor = sharedPreferences.edit()
                    editor.putString("email", emailText.text.toString())
                    editor.putString("password", passwordText.text.toString())

                    editor.apply()
                } else {
                    val editor = sharedPreferences.edit()
                    editor.remove("email")
                    editor.remove("password")
                    emailText.text?.clear()
                    passwordText.text?.clear()
                    editor.apply()
                }

                intent.putExtra(USER_CORRECT_CREDENTIALS, it)

                startActivity(intent)

            } ?: run {

                toastError(this)
            }
        }
    }
}








