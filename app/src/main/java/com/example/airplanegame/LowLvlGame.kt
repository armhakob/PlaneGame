package com.example.airplanegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LowLvlGame:Fragment(R.layout.low_lvl), GameView.GameListener{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.low_lvl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameView: GameView = view.findViewById(R.id.gameView)
        gameView.setGameListener(this)
        gameView.invalidate()
    }

    override fun onGameEnd(won: Boolean) {
        if (won)
            setCurrentFragment(WinnerFragment())
        else
            setCurrentFragment(LoserFragment())
    }

//    override fun onAllBitmapsDeleted(unitsPassed: Int) {
//        TODO("Not yet implemented")
//    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()

}