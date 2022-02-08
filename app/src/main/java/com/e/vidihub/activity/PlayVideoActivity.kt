package com.e.vidihub.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.e.domain.util.Result
import com.e.vidihub.R
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlayVideoActivity : AppCompatActivity() {

    private val viewModel: GetVideoViewModel by viewModels()

    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btFullScreen: ImageView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var flag = false
    private var id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        (this as AppCompatActivity).supportActionBar!!.hide()

        playerView = findViewById(R.id.player_view)
        progressBar = findViewById(R.id.video_progressBar)
        btFullScreen = playerView.findViewById(R.id.bt_fullscreen)

        id = getSharedPreferences("video link", Context.MODE_PRIVATE).getString("key", "")!!

        this@PlayVideoActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        this
            .onBackPressedDispatcher
            .addCallback(this@PlayVideoActivity, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    this@PlayVideoActivity.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        this@PlayVideoActivity.onBackPressed()
                    }
                }
            }
            )

        btFullScreen.setOnClickListener {
            if (flag) {
                btFullScreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen))
                this.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                window.decorView.apply {
                    systemUiVisibility =
                        View.SYSTEM_UI_FLAG_VISIBLE
                }

                flag = false
            } else {
                btFullScreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen_exit))
                this.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                window.decorView.apply {
                    systemUiVisibility =
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                }

                flag = true
            }
        }
        viewModel.playVideo(id)
        observe()

    }

    override fun onRestart() {
        super.onRestart()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.playbackState
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.playbackState
    }


    private fun observe() {
        viewModel.video.observe(this@PlayVideoActivity, {
            when (it) {
                is Result.Success -> {
                    progressBar.visibility = View.GONE


                    val uri: Uri? = Uri.parse(it.data.src)
                    val loadControl: LoadControl = DefaultLoadControl()
                    val bandWidthMeter: BandwidthMeter = DefaultBandwidthMeter()
                    val trackSelector: TrackSelector = DefaultTrackSelector(
                        AdaptiveTrackSelection.Factory(bandWidthMeter)
                    )
                    simpleExoPlayer =
                        ExoPlayerFactory.newSimpleInstance(
                            this@PlayVideoActivity,
                            trackSelector,
                            loadControl
                        )

                    val factory = DefaultHttpDataSourceFactory("exoplayer_video")
//                    val extractorFactory: ExtractorsFactory = DefaultExtractorsFactory()
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
                            when (error!!.type) {
                                ExoPlaybackException.TYPE_SOURCE -> {
                                    Toast.makeText(
                                        this@PlayVideoActivity,
                                        "مشکلی پیش آمده. لطفا پس از پند دقیقه مجددا تلاش کنید",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ExoPlaybackException.TYPE_RENDERER -> {
                                    Toast.makeText(
                                        this@PlayVideoActivity,
                                        "مشکلی پیش آمده. لطفا پس از پند دقیقه مجددا تلاش کنید",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ExoPlaybackException.TYPE_UNEXPECTED -> {
                                    Toast.makeText(
                                        this@PlayVideoActivity,
                                        "مشکلی پیش آمده. لطفا پس از پند دقیقه مجددا تلاش کنید",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
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
                    Toast.makeText(this@PlayVideoActivity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}
