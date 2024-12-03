package com.example.proyectofinal.ui

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.proyectofinal.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    navController: NavHostController,
    videoPath: String, // Ruta del video
    viewModel: NoteViewModel,
    videoId: Int // ID único del video para eliminarlo
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Video", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    Text(
                        text = "Atrás",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { navController.navigateUp() },
                        fontSize = 16.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Decodificar la ruta y reproducir el video
            val decodedVideoPath = Uri.decode(videoPath)
            val videoUri = Uri.parse(decodedVideoPath)

            // Log para ver la URI del video
            Log.d("VideoDetailScreen", "Video URI: $videoUri")

            // Mostrar el video usando VideoView
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(videoUri)

                        // Usar MediaController para controles de reproducción (play, pause, etc.)
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(this)
                        setMediaController(mediaController)

                        // Iniciar la reproducción del video
                        setOnPreparedListener {
                            Log.d("VideoDetailScreen", "Video preparado para reproducir")
                            start()
                        }

                        setOnErrorListener { mp, what, extra ->
                            Log.e("VideoDetailScreen", "Error al reproducir el video: what=$what, extra=$extra")
                            false
                        }

                        setOnCompletionListener {
                            Log.d("VideoDetailScreen", "Reproducción de video completada")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            )

            // Botón para eliminar el video
            Button(
                onClick = {
                    Log.d("VideoDetailScreen", "Botón Eliminar presionado para el video con ID: $videoId")
                    viewModel.deleteVideoById(videoId)
                    navController.navigateUp()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Eliminar", color = Color.White)
            }
        }
    }
}
