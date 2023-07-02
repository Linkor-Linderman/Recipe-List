package com.example.recipe_list.presentation.imageScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipe_list.R
import com.example.recipe_list.presentation.composable.PermissionDialog
import com.example.recipe_list.presentation.composable.WriteExternalStoragePermissionTextProvider
import com.example.recipe_list.presentation.composable.ZoomableImageVariant

@Composable
fun ImageScreen(
    navController: NavController,
    viewModel: ImageScreenViewModel = hiltViewModel()
) {
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val activity = LocalContext.current as Activity
    val writeExternalPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                isGranted = isGranted
            )
        }
    )
    val toastMessages = viewModel.toastMessages.collectAsState(initial = "")

    LaunchedEffect(toastMessages.value) {
        if (toastMessages.value.isNotBlank()) {
            Toast.makeText(activity, toastMessages.value, Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ZoomableImageVariant(
            contentDescription = stringResource(R.string.image),
            model = viewModel.url.value
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back)
                )
            }

            IconButton(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        viewModel.downloadImage()
                    } else {
                        if (ContextCompat.checkSelfPermission(
                                activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.downloadImage()
                        } else {
                            writeExternalPermissionResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.download_icon),
                    modifier = Modifier.size(16.dp),
                    contentDescription = stringResource(R.string.add_file)
                )
            }
        }
        dialogQueue
            .reversed()
            .forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                            WriteExternalStoragePermissionTextProvider(activity)
                        }
                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        activity,
                        permission
                    ),
                    onDismiss = viewModel::dismissDialog,
                    onOkClick = {
                        viewModel.dismissDialog()
                        writeExternalPermissionResultLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    },
                    onGoToAppSettingsClick = { activity.openAppSettings() }
                )
            }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}