package com.example.airplanegame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class UsernameFragment:Fragment(R.layout.username_page) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.username_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.nameEditText)
        val buttonStart = view.findViewById<Button>(R.id.button)
        val typeSpinner = view.findViewById<Spinner>(R.id.typeSpinner)


        editText.clearFocus()
        buttonStart.setOnClickListener{
            val name = editText.text.toString()
            val selectedType = typeSpinner.selectedItem.toString()
            if (name.isNotEmpty() && selectedType.isNotBlank()){
                Game.name = name
                if (selectedType == "Low")
                    setCurrentFragment(LowLvlGame())
                else if (selectedType == "Medium")
                    setCurrentFragment(LowLvlGame()) //TODO:: MidLvlGame()
                else
                    setCurrentFragment(LowLvlGame()) //TODO:: HardLvlGame()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter your name.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
}