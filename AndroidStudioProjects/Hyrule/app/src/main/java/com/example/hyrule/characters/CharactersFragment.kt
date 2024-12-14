package com.example.hyrule.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyrule.databinding.FragmentCharactersBinding
import com.example.hyrule.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


private const val TAG = "CharactersFragment"
class CharactersFragment: Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding cannot be created. Is view created?"
        }

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.characterRecyclerView.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch() {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            getCharacters().collect { hyrulers ->
                binding.characterRecyclerView.adapter = CharacterAdapter(hyrulers)
            }
        }
        }

        binding.addCharacterBtn.setOnClickListener {
            addCharacter()
        }
    }

    private fun getCharacters(): StateFlow<List<Hyrulers>> {
        val listFlow = MutableStateFlow(emptyList<Hyrulers>())

        db.collection("characters")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val characters = snapshot.documents
                        .mapNotNull { doc ->
                            val name = doc.getString("name")
                            if (name != null) Hyrulers(name) else null
                        }
                    listFlow.value = characters
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        return listFlow
    }

    private fun addCharacter () {
        val user = hashMapOf<String, String>(
            "name" to "${binding.characterNameInput.text}"

        )

// Add a new document with a generated ID
        db.collection("characters")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}