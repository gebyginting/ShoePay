package com.geby.shoepay.data.repository

import com.geby.shoepay.utilities.Constants
import com.geby.shoepay.utilities.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signUpUser(name: String, email: String, password: String): ResultState<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            val user = authResult.user
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user?.updateProfile(profileUpdates)?.await()
            val userMap = hashMapOf(
                Constants.KEY_USER_ID to (user?.uid),
                Constants.KEY_NAME to user?.displayName,
                Constants.KEY_EMAIL to email,
                Constants.KEY_PASSWORD to password
            )
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(authResult.user?.uid ?: "")
                .set(userMap)
                .await()

            ResultState.Success("User created successfully")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun loginUser(email: String, password: String): ResultState<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            ResultState.Success("Login successful")
        } catch (e: Exception) {
            ResultState.Error(e.toString())
        }
    }

    suspend fun getUserData(uid: String): ResultState<Map<String, Any>> {
        return try {
            val snapshot = firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(uid)
                .get()
                .await()

            if (snapshot.exists()) {
                ResultState.Success(snapshot.data ?: emptyMap())
            } else {
                ResultState.Error("User not found in Firestore")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

}
