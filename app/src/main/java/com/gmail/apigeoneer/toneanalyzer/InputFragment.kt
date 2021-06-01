package com.gmail.apigeoneer.toneanalyzer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gmail.apigeoneer.toneanalyzer.databinding.FragmentInputBinding
import com.ibm.watson.developer_cloud.dialog.v1.model.Message
import com.ibm.watson.developer_cloud.http.ServiceCallback
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Tone
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions


class InputFragment : Fragment() {

    private lateinit var binding: FragmentInputBinding

    private lateinit var toneAnalyzer: ToneAnalyzer
//    // This handler will only work for the message queue of the Input Fragment
//    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_input, container, false)

        // we need to pass a date, so that the apt version of the tone analyser service is used
        toneAnalyzer = ToneAnalyzer("2021-05-31")
        toneAnalyzer.setApiKey(getString(R.string.tone_analyser_iam_apikey))

        val textToAnalyze = binding.inputEt.text.toString()

        binding.analysisBtn.setOnClickListener {
            analyzeTone(toneAnalyzer, textToAnalyze)
        }

        return binding.root
    }

    private fun analyzeTone(toneAnalyzer: ToneAnalyzer, textToAnalyze: String) {
        // Use the tone analyzer service to list all the emotions
        val toneOptions = ToneOptions.Builder()
            .addTone(Tone.EMOTION)
            .html(false)
            .build()

        toneAnalyzer.getTone(textToAnalyze, toneOptions).enqueue(
            object : ServiceCallback<ToneAnalysis> {
                override fun onFailure(e: Exception?) {
                    Toast.makeText(context, "Failure: ${e?.printStackTrace()}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(response: ToneAnalysis?) {
                    val scores = response?.documentTone?.tones?.get(0)?.tones

                    var detectedTones = ""
                    for (score in scores!!) {
                        if (score.score > 0.5F) {
                            detectedTones += score.name + " "
                        }
                    }

                    val toastMessage = "Expressions expressed: $detectedTones"

                    /**
                     * Since onResponse runs on a different thread, show the toast on the UI thread
                     */
                    activity!!.runOnUiThread(Runnable {                                           // can't use since not an activity
                        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                    })
//                    handler.post(Runnable {
//                        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
//                    })


                }

            }
        )
    }
}