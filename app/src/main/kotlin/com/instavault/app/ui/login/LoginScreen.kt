package com.instavault.app.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instavault.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    sessionExpiredMessage: String? = null,
    onNavigateNext: () -> Unit = {}
) {
    val vaultIdDigits by viewModel.vaultIdDigits.collectAsState()
    val state by viewModel.loginState.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loadingMessage by viewModel.loadingMessage.collectAsState()

    var showSplash by remember { mutableStateOf(sessionExpiredMessage == null) }

    // Auto-Login Check & Splash Screen Handler
    LaunchedEffect(Unit) {
        // First check if user has a valid encrypted session stored
        if (viewModel.checkExistingSession()) {
            onNavigateNext()
            return@LaunchedEffect
        }
        // If no session, show splash briefly then reveal login
        if (sessionExpiredMessage == null) {
            delay(1200)
        }
        showSplash = false
    }

    val bgBrush = Brush.radialGradient(
        colors = listOf(Color(0xFF140D2E), VaultBg),
        center = Offset(0f, 0f),
        radius = 1500f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        // Splash Screen
        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(tween(400)),
            exit = fadeOut(tween(400)) + scaleOut(targetScale = 1.1f, animationSpec = tween(400))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(16.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                        .border(3.dp, VaultPurpleLight.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.FlashOn, contentDescription = "Logo", tint = VaultWhite, modifier = Modifier.size(52.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("InstaVault", color = VaultWhite, fontSize = 34.sp, fontWeight = FontWeight.Black)
            }
        }

        // Main Screen
        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(600, delayMillis = 200)),
            exit = fadeOut()
        ) {
            if (state == LoginState.SUCCESS) {
                LaunchedEffect(Unit) {
                    delay(1500)
                    onNavigateNext()
                }
                SuccessView(userName = userName ?: "", onReset = { viewModel.reset() })
            } else {
                LoginView(
                    vaultIdDigits = vaultIdDigits,
                    state = state,
                    errorMessage = errorMessage,
                    sessionExpiredMessage = sessionExpiredMessage,
                    loadingMessage = loadingMessage,
                    onVaultIdChange = viewModel::onVaultIdChange,
                    onConnect = viewModel::onConnect,
                    onFillDemo = viewModel::onFillDemo
                )
            }
        }
    }
}

@Composable
fun SuccessView(userName: String, onReset: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .shadow(20.dp, CircleShape)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VaultGreen, Color(0xFF00E676))))
                .border(3.dp, VaultGreen.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Success", tint = VaultWhite, modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome Back!",
            color = VaultGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = userName,
            color = VaultWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Vault Authenticated & Session Encrypted",
            color = VaultGreyLight,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginView(
    vaultIdDigits: String,
    state: LoginState,
    errorMessage: String?,
    sessionExpiredMessage: String?,
    loadingMessage: String,
    onVaultIdChange: (String) -> Unit,
    onConnect: () -> Unit,
    onFillDemo: (String) -> Unit
) {
    val isFilled = vaultIdDigits.isNotEmpty()
    val haptic = LocalHapticFeedback.current

    var showHelp by remember { mutableStateOf(false) }
    var showDemo by remember { mutableStateOf(false) }

    // Error Shake Animation
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == LoginState.ERROR) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            for (i in 0..2) {
                shakeOffset.animateTo(15f, tween(50))
                shakeOffset.animateTo(-15f, tween(50))
            }
            shakeOffset.animateTo(0f, tween(50))
        }
    }

    if (showHelp) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showHelp = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(VaultCard, Color(0xFF1A142A))))
                    .border(1.dp, VaultPurple.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = VaultGold, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Find Your Vault ID", color = VaultGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Go to the InstaVault Telegram Bot and type /profile. You will find your unique VLT ID there.",
                        color = VaultGreyLight,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { showHelp = false },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VaultPurple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Got it", color = VaultWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                .border(2.dp, VaultPurpleLight.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.FlashOn, contentDescription = "Logo", tint = VaultWhite, modifier = Modifier.size(38.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("InstaVault", color = VaultWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enter Your Vault ID", color = VaultGreyLight, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                Icons.Filled.HelpOutline,
                contentDescription = "Help",
                tint = VaultGold,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { showHelp = true }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Variable-length Vault ID input. Bot IDs are VLT- + Telegram user ID,
        // so the numeric suffix is not limited to five digits.
        BasicTextField(
            value = vaultIdDigits,
            onValueChange = onVaultIdChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .offset(x = shakeOffset.value.dp),
            textStyle = LocalTextStyle.current.copy(
                color = if (state == LoginState.ERROR) VaultRed else VaultWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(VaultGold),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VaultCard)
                        .border(
                            1.dp,
                            when {
                                state == LoginState.ERROR -> VaultRed
                                vaultIdDigits.isNotEmpty() -> VaultPurpleLight
                                else -> VaultPurple.copy(alpha = 0.2f)
                            },
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("VLT-", color = VaultGold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (vaultIdDigits.isEmpty()) {
                            Text("Enter Telegram ID", color = VaultGrey, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Status & Error Display Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            val assembled = vaultIdDigits
            if (isFilled && state == LoginState.IDLE) {
                Text(
                    text = "VLT-$assembled",
                    color = VaultGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VaultGold.copy(alpha = 0.08f))
                        .border(1.dp, VaultGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            } else if (state == LoginState.ERROR) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VaultRed.copy(alpha = 0.1f))
                        .border(1.dp, VaultRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Filled.Error, contentDescription = null, tint = VaultRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = errorMessage ?: "Invalid Vault ID",
                        color = VaultRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (sessionExpiredMessage != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VaultGold.copy(alpha = 0.1f))
                        .border(1.dp, VaultGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Filled.Error, contentDescription = null, tint = VaultGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sessionExpiredMessage,
                        color = VaultGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (state == LoginState.LOADING) {
                Text(
                    text = loadingMessage,
                    color = VaultPurpleLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Button Morphing Animation
        val buttonWidth by animateDpAsState(
            targetValue = if (state == LoginState.LOADING) 56.dp else 300.dp,
            animationSpec = tween(300, easing = FastOutSlowInEasing),
            label = "btnWidth"
        )

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(animation = tween(700, easing = LinearEasing)),
            label = "spinner"
        )

        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onConnect()
            },
            enabled = isFilled && state != LoginState.LOADING,
            modifier = Modifier
                .width(buttonWidth)
                .height(56.dp)
                .shadow(if (isFilled) 12.dp else 0.dp, CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = VaultWhite.copy(alpha = 0.05f)
            ),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isFilled && state != LoginState.LOADING) Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight))
                        else SolidColor(Color.Transparent)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (state == LoginState.LOADING) {
                    CircularProgressIndicator(
                        color = VaultWhite,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp).rotate(rotation)
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Link, contentDescription = null, tint = if (isFilled) VaultWhite else VaultGrey, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connect My Account", color = if (isFilled) VaultWhite else VaultGrey, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        TextButton(onClick = { showDemo = !showDemo }) {
            Text(if (showDemo) "Hide Demo Accounts" else "Try Demo Account", color = VaultGrey, fontSize = 11.sp)
        }

        AnimatedVisibility(visible = showDemo) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val mocks = listOf("VLT-7437014244", "VLT-5638105912", "VLT-5746912333")
                mocks.forEach { id ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(VaultPurple.copy(alpha = 0.1f))
                            .border(1.dp, VaultPurple.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onFillDemo(id)
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(id, color = VaultPurpleLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Real Vault User", color = VaultGrey, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
