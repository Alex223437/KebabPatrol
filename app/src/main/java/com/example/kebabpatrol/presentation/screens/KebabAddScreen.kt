package com.example.kebabpatrol.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kebabpatrol.presentation.navigation.Screen
import com.example.kebabpatrol.presentation.viewmodel.KebabAddViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KebabAddScreen(
    navController: NavController,
    lat: Double,
    lng: Double,
    viewModel: KebabAddViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is KebabAddViewModel.UiEvent.ShowSnackbar -> {
                    keyboardController?.hide()
                    snackbarHostState.showSnackbar(event.message)
                }
                is KebabAddViewModel.UiEvent.Success -> {
                    keyboardController?.hide()

                    Toast.makeText(context, "TARGET LOCKED! DATA SAVED 🍖", Toast.LENGTH_LONG).show()

                    navController.navigate(Screen.List.route) {
                        popUpTo(Screen.List.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REPORT LOCATION", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RectangleShape
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF212121))
                    .border(1.dp, Color(0xFF333333))
                    .padding(16.dp)
            ) {
                Text("TACTICAL DATA", color = Color(0xFF757575), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Text("LAT: $lat", color = Color.White, fontFamily = FontFamily.Monospace)
                Text("LNG: $lng", color = Color.White, fontFamily = FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("TARGET NAME", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = viewModel.name.value,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF212121), unfocusedContainerColor = Color(0xFF212121), focusedTextColor = Color.White, unfocusedTextColor = Color.Gray, cursorColor = Color(0xFFD32F2F), focusedIndicatorColor = Color(0xFFD32F2F), unfocusedIndicatorColor = Color.Transparent),
                shape = RectangleShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("INTEL (DESCRIPTION)", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = viewModel.description.value,
                onValueChange = viewModel::onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF212121), unfocusedContainerColor = Color(0xFF212121), focusedTextColor = Color.White, unfocusedTextColor = Color.Gray, cursorColor = Color(0xFFD32F2F), focusedIndicatorColor = Color(0xFFD32F2F), unfocusedIndicatorColor = Color.Transparent),
                shape = RectangleShape,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("RATING (1.0 - 5.0)", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = viewModel.rating.value,
                onValueChange = viewModel::onRatingChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF212121), unfocusedContainerColor = Color(0xFF212121), focusedTextColor = Color(0xFFFFC107), unfocusedTextColor = Color(0xFFFFC107), cursorColor = Color(0xFFD32F2F), focusedIndicatorColor = Color(0xFFD32F2F), unfocusedIndicatorColor = Color.Transparent),
                shape = RectangleShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.saveKebab(lat, lng) },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F), disabledContainerColor = Color(0xFF550000)),
                shape = RectangleShape
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("CONFIRM LOCATION", fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}