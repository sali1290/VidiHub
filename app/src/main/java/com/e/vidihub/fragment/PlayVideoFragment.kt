package com.e.vidihub.fragment

import android.app.ProgressDialog
import android.graphics.PixelFormat
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.e.vidihub.databinding.FragmentPlayVideoBinding


class PlayVideoFragment : Fragment() {

    private lateinit var binding: FragmentPlayVideoBinding

    var mediaController: MediaController? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playVideo("https://youtu.be/RLmsC5j-5SI")
    }

    private fun playVideo(videopath: String) {
        Log.e("entered", "playvide")
        Log.e("path is", "" + videopath)
        try {
            progressDialog = ProgressDialog.show(
                requireContext(), "",
                "Buffering video...", false
            )
            progressDialog!!.setCancelable(true)

            mediaController = MediaController(requireContext())
            val video = Uri.parse(videopath)
            binding.videoView.setMediaController(mediaController)
            binding.videoView.setVideoURI(video)
            binding.videoView.setOnPreparedListener(OnPreparedListener {
                progressDialog!!.dismiss()
                binding.videoView.start()
            })
        } catch (e: Exception) {
            progressDialog!!.dismiss()
            println("Video Play Error :" + e.message)
        }
    }


}