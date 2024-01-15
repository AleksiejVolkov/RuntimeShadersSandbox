package com.offmind.ringshaders.repository.audio

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener


class AudioToTextureProcessor(private val context: Context) {
    private val mediaPlayer by lazy { MediaPlayer() }
    fun processAudioFile(filePath: String) {
        val sampleRate = 44100
        val audioBufferSize = 1024
        val bufferOverlap = 0

        val afd: AssetFileDescriptor = context.assets.openFd(filePath)

        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.prepare()

        mediaPlayer.start()

        val visualizer = Visualizer(mediaPlayer.audioSessionId)
        println("HUI ${mediaPlayer.audioSessionId}")

       /* visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1])

        visualizer.setDataCaptureListener(object : OnDataCaptureListener {
            override fun onWaveFormDataCapture(
                visualizer: Visualizer, bytes: ByteArray,
                samplingRate: Int
            ) {
                println("HUI ${bytes.size}")
            }

            override fun onFftDataCapture(
                visualizer: Visualizer, bytes: ByteArray,
                samplingRate: Int
            ) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false)

        visualizer.setEnabled(true)*/
    }
}