package com.instavault.app.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instavault.app.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateNext: () -> Unit = {}
) {
    val digits by viewModel.digits.collectAsState()
    val state by viewModel.loginState.collectAsState()
    val userName by viewModel.userName.collectAsState()

    val bgBrush = Brush.radialGradient(
        colors = listOf(Color(0xFF0D0720), Color(0xFF050510)),
        center = Offset(0f, 0f),
        radius = 1500f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "⚡ INSTAVAULT — LOGIN SCREEN",
                color = Color(0xFF333333),
                fontSize = 12.sp,
                letterSpacing = 3.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )

            // Phone Frame
            Box(
                modifier = Modifier
                    .width(375.dp)
                    .height(780.dp)
                    .clip(RoundedCornerShape(52.dp))
                    .background(VaultBg)
                    .border(8.dp, Color(0xFF111111), RoundedCornerShape(52.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Status Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("9:41", color = VaultGrey, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("📶", fontSize = 12.sp)
                            Text("🔋", fontSize = 12.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (state == LoginState.SUCCESS) {
                            SuccessView(userName = userName ?: "", onReset = { viewModel.reset() })
                        } else {
                            LoginView(
                                digits = digits,
                                state = state,
                                onDigitChange = viewModel::onDigitChange,
                                onConnect = viewModel::onConnect,
                                onFillDemo = viewModel::onFillDemo
                            )
                        }
                    }
                }
            }
            
            Text(
                text = "Try: 00001 • 00847 • 01234",
                color = Color(0xFF333333),
                fontSize = 11.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
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
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VaultGreen.copy(alpha = 0.2f), VaultGreen.copy(alpha = 0.07f))))
                .border(3.dp, VaultGreen.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✅", fontSize = 40.sp)
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
                .height(56.dp),
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
                Text("🚀 Enter InstaVault", color = VaultWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
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
    onFillDemo: (String) -> Unit
) {
    val isFilled = digits.all { it.isNotEmpty() }
    
    val focusRequesters = remember { List(5) { FocusRequester() } }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 40.dp)
    ) {
        // Top Logo
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                    .border(2.dp, VaultPurpleLight.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("⚡", fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("InstaVault", color = VaultWhite, fontSize = 26.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(6.dp))
            Text("CONNECT YOUR ACCOUNT", color = VaultGrey, fontSize = 12.sp, letterSpacing = 1.sp)
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Instructions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VaultGold.copy(alpha = 0.06f))
                .border(1.dp, VaultGold.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text("💡", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Vault ID kahan milega?", color = VaultGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))
                Text("Telegram Bot → /profile → #VLT-XXXXX", color = VaultGreyLight, fontSize = 11.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Apna 5-digit Vault ID enter karo",
            color = VaultGreyLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
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
                        if (it.length <= 1) {
                            onDigitChange(index, it)
                            if (it.isNotEmpty() && index < 4) {
                                focusRequesters[index + 1].requestFocus()
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val assembled = digits.joinToString("")
            if (isFilled) {
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
            } else {
                Text(
                    text = "VLT-${digits.joinToString("") { it.ifEmpty { "_" } }}",
                    color = VaultGrey,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        
        AnimatedVisibility(visible = state == LoginState.ERROR) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VaultRed.copy(alpha = 0.09f))
                    .border(1.dp, VaultRed.copy(alpha = 0.27f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("❌ Invalid Vault ID", color = VaultRed, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "spinner"
        )

        Button(
            onClick = onConnect,
            enabled = isFilled && state != LoginState.LOADING,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = VaultWhite.copy(alpha = 0.05f)
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp)
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = VaultWhite,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(18.dp)
                                .rotate(rotation)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Connecting...", color = VaultGrey, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    }
                } else {
                    Text("🔗 Connect My Account", color = if (isFilled) VaultWhite else VaultGrey, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(VaultWhite.copy(alpha = 0.06f)))
            Spacer(modifier = Modifier.width(12.dp))
            Text("DEMO MODE", color = VaultGrey, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f).height(1.dp).background(VaultWhite.copy(alpha = 0.06f)))
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Try karo — koi bhi ID click karo:", color = VaultGrey, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val mocks = listOf("VLT-00001", "VLT-00847", "VLT-01234")
            mocks.forEach { id ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VaultPurple.copy(alpha = 0.1f))
                        .border(1.dp, VaultPurple.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .clickable { onFillDemo(id) }
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
