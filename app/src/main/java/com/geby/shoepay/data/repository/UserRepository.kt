package com.geby.shoepay.data.repository

import com.geby.shoepay.data.models.PaymentHistory
import com.geby.shoepay.utilities.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserHistories(uid: String): ResultState<List<PaymentHistory>> {
        return try {
            val snapshot = firestore
                .collection("payment_histories")
                .whereEqualTo("userId", uid)
                .get()
                .await()

            val data = snapshot.documents.mapNotNull {
                it.toObject(PaymentHistory::class.java)
            }

            ResultState.Success(data)

        }  catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }
}