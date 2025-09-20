package com.example.sp.panic

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun start(maxSeconds: Int = 30): File {
        stop()
        val file = File.createTempFile("panic_", ".m4a", context.cacheDir)
        outputFile = file
        val r = if (Build.VERSION.SDK_INT >= 31) MediaRecorder(context) else MediaRecorder()
        recorder = r
        r.setAudioSource(MediaRecorder.AudioSource.MIC)
        r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        r.setAudioEncodingBitRate(128_000)
        r.setAudioSamplingRate(44_100)
        r.setOutputFile(file.absolutePath)
        r.setMaxDuration(maxSeconds * 1000)
        r.prepare()
        r.start()
        return file
    }

    fun stop(): File? {
        return try {
            recorder?.apply {
                try { stop() } catch (_: Exception) {}
                reset()
                release()
            }
            recorder = null
            outputFile
        } catch (_: Exception) {
            outputFile
        } finally {
            outputFile = null
        }
    }
}


