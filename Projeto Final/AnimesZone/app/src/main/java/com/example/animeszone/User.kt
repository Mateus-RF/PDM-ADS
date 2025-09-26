package com.example.animeszone
import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = ""
)


class AuthService {

    val auth: FirebaseAuth by lazy { Firebase.auth }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    fun registerUser(
        activity: Activity,
        username: String,
        email: String,
        password: String,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->

                fun callCallback(success: Boolean, message: String) {
                    activity.runOnUiThread {
                        onComplete(success, message)
                    }
                }

                if (!authTask.isSuccessful) {
                    val errorMessage = authTask.exception?.message ?: "Erro desconhecido."
                    callCallback(false, "Falha no cadastro: $errorMessage")
                    return@addOnCompleteListener
                }

                // Usuário criado com sucesso
                val firebaseUser = auth.currentUser
                if (firebaseUser == null) {
                    callCallback(false, "Erro: Usuário não encontrado após cadastro.")
                    return@addOnCompleteListener
                }

                // Chama callback de sucesso imediatamente
                callCallback(true, "Cadastro realizado com sucesso!")

                // Salva perfil no Firestore em segundo plano (não bloqueia a tela)
                val userProfile = User(
                    uid = firebaseUser.uid,
                    username = username,
                    email = email
                )
                db.collection("users")
                    .document(firebaseUser.uid)
                    .set(userProfile)
                    .addOnSuccessListener {
                        Log.d("AuthService", "Perfil salvo no Firestore com sucesso.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("AuthService", "Falha ao salvar perfil no Firestore", e)
                    }
            }
    }



    fun signInUser(
        email: String,
        password: String,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthService", "Login bem-sucedido.")
                    onComplete(true, "Login bem-sucedido!")
                } else {
                    Log.w("AuthService", "Erro no login", task.exception)
                    val errorMessage = task.exception?.message ?: "Erro desconhecido."
                    onComplete(false, "Falha no login: $errorMessage")
                }
            }
    }
    fun getCurrentUserUsername(onResult: (String?) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username")
                        onResult(username)
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("AuthService", "Erro ao obter username", e)
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }


}

