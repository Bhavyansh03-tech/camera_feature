package com.example.camera_feature

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.camera_feature.ui.theme.CameraFeatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Creating variable to store image :->
            val imageStoring = remember {
                mutableStateOf<Bitmap?>(null)
            }

            // Camera Launcher :->
            val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
                // Image will get in bitmap format :->
                imageStoring.value = it
            }

            // Permission Launcher :->
            val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

                // If Permission is granted :->
                if (it){
                    cameraLauncher.launch()
                }

            }

            CameraFeatureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Displaying Image :->
                        imageStoring.value.let {
                            if (it != null){
                                Image(bitmap = it.asImageBitmap(), contentDescription = null)
                            }
                        }

                        // Handling button to launch camera :->
                        val context = LocalContext.current
                        TextButton(onClick = {
                            // Checking that permission is granted or not :->
                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                                // If granted launch camera :->
                                cameraLauncher.launch()
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        }) {
                            // Showing Text :->
                            Image(Icons.Rounded.Warning, contentDescription = null)
                            Text(text = "CLICK TO OPEN CAMERA")
                        }
                    }
                }
            }
        }
    }
}