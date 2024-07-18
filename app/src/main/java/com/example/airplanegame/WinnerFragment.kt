package com.example.airplanegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class WinnerFragment:Fragment(R.layout.winner_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.winner_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retryBtn = view.findViewById<Button>(R.id.retry)

        retryBtn.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}