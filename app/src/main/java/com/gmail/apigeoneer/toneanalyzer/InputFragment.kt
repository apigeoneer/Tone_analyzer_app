package com.gmail.apigeoneer.toneanalyzer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gmail.apigeoneer.toneanalyzer.databinding.FragmentInputBinding
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer

class InputFragment : Fragment() {

    private lateinit var binding: FragmentInputBinding

    private lateinit var toneAnalyser: ToneAnalyzer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_input, container, false)

        // we need to pass a date, so that the apt version of the tone analyser service is used
        toneAnalyser = ToneAnalyzer("2021-05-31")
        toneAnalyser.setApiKey(getString(R.string.tone_analyser_iam_apikey))

        return binding.root
    }
}