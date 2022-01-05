package com.e.vidihub.fragment

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.domain.util.Result
import com.e.vidihub.R
import com.e.vidihub.databinding.FragmentPlayVideoBinding
import com.e.vidihub.viewmodel.GetVideoViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory


class PlayVideoFragment : Fragment() {

    private lateinit var binding: FragmentPlayVideoBinding
    private lateinit var viewModel: GetVideoViewModel

    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btFullScreen: ImageView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var flag = false
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[GetVideoViewModel::class.java]

        playerView = binding.playerView
        progressBar = binding.videoProgressBar
        btFullScreen = playerView.findViewById(R.id.bt_fullscreen)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        id = arguments?.getString("video link")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    requireActivity().requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    this@PlayVideoFragment.onDestroy()
                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )

        viewModel.playVideo(id)
        observe()


        btFullScreen.setOnClickListener {
            if (flag) {
                btFullScreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen))
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                flag = false
            } else {
                btFullScreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen_exit))
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                flag = true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.playbackState
    }

    private fun observe() {
        viewModel.video.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    progressBar.visibility = View.GONE


                    var uri: Uri? = Uri.parse(it.data.src)
                    val loadControl: LoadControl = DefaultLoadControl()
                    val bandWidthMeter: BandwidthMeter = DefaultBandwidthMeter()
                    val trackSelector: TrackSelector = DefaultTrackSelector(
                        AdaptiveTrackSelection.Factory(bandWidthMeter)
                    )
                    simpleExoPlayer =
                        ExoPlayerFactory.newSimpleInstance(
                            requireContext(),
                            trackSelector,
                            loadControl
                        )

                    val factory = DefaultHttpDataSourceFactory("exoplayer_video")
                    val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
//                    var mediaSource: MediaSource =
//                        HlsMediaSource.Factory(
//                            factory,
//                            uri,
//                            extractorFactory,
//                            null,
//                            null
//                        )
                    val mediaSource =
                        HlsMediaSource.Factory(factory)
                            .createMediaSource(uri)

                    playerView.player = simpleExoPlayer
                    playerView.keepScreenOn = true
                    simpleExoPlayer.prepare(mediaSource)
                    simpleExoPlayer.playWhenReady = true
                    simpleExoPlayer.addListener(object : Player.EventListener {
                        override fun onTimelineChanged(
                            timeline: Timeline?,
                            manifest: Any?,
                            reason: Int
                        ) {
                        }

                        override fun onTracksChanged(
                            trackGroups: TrackGroupArray?,
                            trackSelections: TrackSelectionArray?
                        ) {
                        }

                        override fun onLoadingChanged(isLoading: Boolean) {
                        }

                        override fun onPlayerStateChanged(
                            playWhenReady: Boolean,
                            playbackState: Int
                        ) {
                            if (playbackState == Player.STATE_BUFFERING) {
                                progressBar.visibility = View.VISIBLE
                            } else if (playbackState == Player.STATE_READY) {
                                progressBar.visibility = View.GONE
                            }
                        }

                        override fun onRepeatModeChanged(repeatMode: Int) {
                        }

                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                        }

                        override fun onPlayerError(error: ExoPlaybackException?) {
                        }

                        override fun onPositionDiscontinuity(reason: Int) {
                        }

                        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                        }

                        override fun onSeekProcessed() {
                        }

                    })


                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}