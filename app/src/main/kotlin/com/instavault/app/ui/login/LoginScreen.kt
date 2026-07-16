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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
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
    onNavigateNext: () -> Unit = {}
) {
    val digits by viewModel.digits.collectAsState()
    val state by viewModel.loginState.collectAsState()
    val userName by viewModel.userName.collectAsState()

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200)
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
                    digits = digits,
                    state = state,
                    onDigitChange = viewModel::onDigitChange,
                    onConnect = viewModel::onConnect,
                    onFillDemo = viewModel::onFillDemo,
                    onPaste = viewModel::onPaste
                )
            }
        }
    }
}

@Composable
fun SuccessView(userName: String, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VaultGreen.copy(alpha = 0.2f), VaultGreen.copy(alpha = 0.07f))))
                .border(3.dp, VaultGreen.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = "Success", tint = VaultWhite, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Connected!", color = VaultWhite, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Welcome back, $userName!", color = VaultGreyLight, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(28.dp))

        // User Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(VaultPurple.copy(alpha = 0.13f), VaultCard)))
                .border(1.dp, VaultPurple.copy(alpha = 0.27f), RoundedCornerShape(24.dp))
                .padding(22.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                            .border(2.dp, VaultGold.copy(alpha = 0.27f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(userName.take(1), color = VaultWhite, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(userName, color = VaultWhite, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Text("VLT-*****", color = VaultGrey, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(VaultGreen.copy(alpha = 0.13f))
                            .border(1.dp, VaultGreen.copy(alpha = 0.27f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("● Connected", color = VaultGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* Navigate to Home/Vault */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight))),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.RocketLaunch, contentDescription = null, tint = VaultWhite, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enter InstaVault", color = VaultWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onReset) {
            Text("← Dusra account use karo", color = VaultGrey, fontSize = 13.sp)
        }
    }
}

@Composable
fun LoginView(
    digits: List<String>,
    state: LoginState,
    onDigitChange: (Int, String) -> Unit,
    onConnect: () -> Unit,
    onFillDemo: (String) -> Unit,
    onPaste: (String) -> Unit
) {
    val isFilled = digits.all { it.isNotEmpty() }
    val focusRequesters = remember { List(5) { FocusRequester() } }
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
                        "Go to the InstaVault Telegram Bot and type /profile. You will find your unique #VLT-XXXXX ID there.",
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
            Icon(Icons.Filled.FlashOn, contentDescription = null, tint = VaultWhite, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("InstaVault", color = VaultWhite, fontSize = 26.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(6.dp))
        Text("CONNECT YOUR ACCOUNT", color = VaultGrey, fontSize = 12.sp, letterSpacing = 1.sp)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Enter your 5-digit Vault ID",
                color = VaultGreyLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { 
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    showHelp = true 
                },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = VaultGrey, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        
        // Input Row with Shake Animation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = shakeOffset.value.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(VaultPurple.copy(alpha = 0.13f))
                    .border(1.5f.dp, VaultPurple.copy(alpha = 0.27f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text("VLT-", color = VaultPurple, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            
            digits.forEachIndexed { index, d ->
                BasicTextField(
                    value = d,
                    onValueChange = { 
                        if (it.length > 1) { // Handle Paste
                            val digitsPasted = it.filter { char -> char.isDigit() }
                            if (digitsPasted.length == 5) {
                                onPaste(digitsPasted)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onConnect() // Auto-connect
                            }
                        } else if (it.length <= 1) {
                            onDigitChange(index, it)
                            if (it.isNotEmpty()) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (it.isNotEmpty() && index < 4) {
                                focusRequesters[index + 1].requestFocus()
                            }
                            if (it.isNotEmpty() && index == 4) {
                                // Auto-submit when last digit is typed
                                onConnect()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = if (index < 4) 10.dp else 0.dp)
                        .size(width = 44.dp, height = 54.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { 
                            if (it.key == Key.Backspace && d.isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        }
                        .shadow(if (d.isNotEmpty()) 8.dp else 0.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (d.isNotEmpty()) VaultPurple.copy(alpha = 0.13f) else VaultCard)
                        .border(
                            1.5f.dp, 
                            if (state == LoginState.ERROR) VaultRed.copy(alpha = 0.6f) 
                            else if (d.isNotEmpty()) VaultPurple.copy(alpha = 0.53f) 
                            else VaultWhite.copy(alpha = 0.08f),
                            RoundedCornerShape(12.dp)
                        ),
                    textStyle = TextStyle(
                        color = VaultGold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    cursorBrush = SolidColor(VaultGold),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            innerTextField()
                        }
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().height(24.dp), contentAlignment = Alignment.Center) {
            val assembled = digits.joinToString("")
            if (isFilled && state != LoginState.ERROR) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Error, contentDescription = null, tint = VaultRed, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invalid Vault ID", color = VaultRed, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
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
        
        Spacer(modifier = Modifier.height(32.dp))
        
        TextButton(onClick = { showDemo = !showDemo }) {
            Text(if (showDemo) "Hide Demo Accounts" else "Try Demo Account", color = VaultGrey, fontSize = 11.sp)
        }
        
        AnimatedVisibility(visible = showDemo) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val mocks = listOf("VLT-00001", "VLT-00847", "VLT-01234")
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
                        Text("Demo User", color = VaultGrey, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
