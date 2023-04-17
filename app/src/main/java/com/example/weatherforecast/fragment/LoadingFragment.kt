package com.example.weatherforecast.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.R

class LoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    lateinit var progressBar:ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.progressBar)

        //Setting up progress bar (Fake loading currently)
        progressBar()
    }

    private fun progressBar(){

        progressBar.max = 10000
        val currentProgress = 10000

        val animation = ObjectAnimator.ofInt(progressBar,
            "progress",
            currentProgress)
            .setDuration(3000)


        animation.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                findNavController().navigate(R.id.action_loadingFragment_to_weatherFragment)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

        })

        animation.start()
    }

}