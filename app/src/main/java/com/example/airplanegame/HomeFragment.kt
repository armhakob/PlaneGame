package com.example.airplanegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.home_page) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnStartGame = view.findViewById<Button>(R.id.buttonStart)

        btnStartGame.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, UsernameFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}