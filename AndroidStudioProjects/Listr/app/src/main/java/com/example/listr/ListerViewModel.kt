package com.example.listr

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val TAG = "ListrViewModel"

class ListrViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _naughtyList = MutableStateFlow<List<ListItem>>(emptyList())
    val naughtyList: StateFlow<List<ListItem>> = _naughtyList

    private val _niceList = MutableStateFlow<List<ListItem>>(emptyList())
    val niceList: StateFlow<List<ListItem>> = _niceList


    private val randomNames = listOf(
        "Aazaan", "Abaan", "Abbas",  "Benton", "Berg", "Berger", "George",
        "Hannah", "Dunlap", "Dunn", "Duran", "Durham", "Lily", "Michael", "Nina",
        "Matthews", "Maxwell", "May", "Mayer"
    )

    init {
        fetchNaughtyList()
        fetchNiceList()
    }

    private fun fetchNaughtyList() {
        db.collection("naughty")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val items = snapshot.documents
                        .mapNotNull { doc ->
                            val name = doc.getString("name")
                            if (name != null) ListItem(name, doc.id) else null
                        }
                    _naughtyList.value = items
                }
            }
    }

    private fun fetchNiceList() {
        db.collection("nice")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val items = snapshot.documents
                        .mapNotNull { doc ->
                            val name = doc.getString("name")
                            if (name != null) ListItem(name, doc.id) else null
                        }
                    _niceList.value = items
                }
            }
    }

    fun addToNaughtyList(name: String) {
        val person = hashMapOf("name" to name)
        db.collection("naughty")
            .add(person)
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun addToNiceList(name: String) {
        val person = hashMapOf("name" to name)
        db.collection("nice")
            .add(person)
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun addRandomToNaughtyList() {
        val randomName = randomNames.random()
        addToNaughtyList(randomName)
    }

    fun addRandomToNiceList() {
        val randomName = randomNames.random()
        addToNiceList(randomName)
    }
}