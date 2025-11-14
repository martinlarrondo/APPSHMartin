package com.example.myapplication

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConfig {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}
