package com.example.sp.panic

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PanicScreen(
    locationFlow: Flow<Location?>,
    onRecordingFinished: (File?, Location?) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val audioRecorder = remember(context) { AudioRecorder(context) }
    val locationState: MutableState<Location?> = remember { mutableStateOf(null) }
    val timer: MutableState<Int> = remember { mutableStateOf(30) }
    val isRecording = remember { mutableStateOf(false) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) startRecording(scope, audioRecorder, timer, isRecording, onRecordingFinished, locationState.value)
        }
    )

    LaunchedEffect(Unit) {
        locationFlow.filterNotNull().collect { locationState.value = it }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Recording will auto-stop in ${'$'}{timer.value}s")
            Button(onClick = {
                if (!isRecording.value) {
                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                } else {
                    val file = audioRecorder.stop()
                    isRecording.value = false
                    onRecordingFinished(file, locationState.value)
                }
            }) {
                Text(if (isRecording.value) "Stop" else "Start Panic Recording")
            }
        }
    }
}

private fun startRecording(
    scope: CoroutineScope,
    audioRecorder: AudioRecorder,
    timer: MutableState<Int>,
    isRecording: MutableState<Boolean>,
    onRecordingFinished: (File?, Location?) -> Unit,
    lastLocation: Location?
) {
    isRecording.value = true
    val file = audioRecorder.start(maxSeconds = 30)
    scope.launch {
        for (i in 29 downTo 0) {
            delay(1000)
            timer.value = i
        }
        val out = audioRecorder.stop()
        isRecording.value = false
        onRecordingFinished(out ?: file, lastLocation)
    }
}


