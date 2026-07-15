package com.instavault.app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToGames: () -> Unit = {},
    onNavigateToSpin: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBg)
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item { HomeHeader() }
        item { Spacer(modifier = Modifier.height(18.dp)) }
        item { HeroBalanceCard() }
        item { Spacer(modifier = Modifier.height(14.dp)) }
        item { DailyTaskBanner(onNavigateToTasks) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { SectionTitle("QUICK ACTIONS") }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item { QuickActionsGrid(onNavigateToGames, onNavigateToSpin) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { SectionTitle("RECENT ACTIVITY") }
        item { Spacer(modifier = Modifier.height(10.dp)) }
        item { RecentActivityList() }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        justifyContent = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome back,", color = VaultGrey, fontSize = 12.sp)
            Text("Rahul 👋", color = VaultWhite, fontSize = 22.sp, fontWeight = FontWeight.Black)
        }
        
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text("🔔", fontSize = 20.sp)
            
            // Notification Badge
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp)
                    .clip(CircleShape)
                    .background(VaultRed)
                    .border(2.dp, VaultBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun HeroBalanceCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = VaultPurple.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF2A0870), Color(0xFF0D0D30), Color(0xFF1A0A3A))
                )
            )
            .border(1.dp, VaultPurple.copy(alpha = 0.33f), RoundedCornerShape(26.dp))
            // Inner shadow simulation via border or background glow
    ) {
        // Absolute glow blobs using drawBehind
        Box(modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(VaultGold.copy(alpha = 0.15f), Color.Transparent),
                        radius = 160.dp.toPx()
                    ),
                    radius = 160.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(size.width + 40.dp.toPx(), -40.dp.toPx())
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(VaultPurple.copy(alpha = 0.2f), Color.Transparent),
                        radius = 100.dp.toPx()
                    ),
                    radius = 100.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(-20.dp.toPx(), size.height + 20.dp.toPx())
                )
            }
            .padding(top = 22.dp, bottom = 18.dp, start = 22.dp, end = 22.dp)
        ) {
            Column {
                Text("TOTAL BALANCE", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("⚡ 1,700 ", color = VaultGold, fontSize = 38.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                    Text("Sparks", color = VaultGold.copy(alpha = 0.8f), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagItem("🔥 Day 7 Streak", VaultGold)
                    TagItem("🥉 Rookie", VaultGreyLight)
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                Text("Rising Creator mein 910 RP aur", color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                CustomProgressBar(progress = 0.9f, color = VaultGold)
            }
        }
    }
}

@Composable
fun DailyTaskBanner(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = VaultGold.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1A0832), Color(0xFF0F0F28))))
            .border(1.dp, VaultGold.copy(alpha = 0.27f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            justifyContent = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚡ ", fontSize = 16.sp)
                    Text("AAJKA TASK ", color = VaultWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                    Box(
                        modifier = Modifier
                            .background(VaultRed.copy(alpha = 0.13f), RoundedCornerShape(6.dp))
                            .border(1.dp, VaultRed.copy(alpha = 0.27f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text("LIVE", color = VaultRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("5 tasks • ⚡ 400 Sparks • ⏰ 08:23:10", color = VaultGrey, fontSize = 12.sp)
            }
            
            Box(
                modifier = Modifier
                    .background(Brush.linearGradient(listOf(VaultGold, VaultGoldLight)), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text("Go →", color = VaultBg, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = VaultGrey,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )
}

@Composable
fun QuickActionsGrid(onNavigateToGames: () -> Unit, onNavigateToSpin: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionCard("🎮", "Games", "Bonus Sparks!", "purple", Modifier.weight(1f)) { onNavigateToGames() }
            ActionCard("🎰", "Spin Wheel", "1 Free Spin!", "gold", Modifier.weight(1f)) { onNavigateToSpin() }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionCard("🎁", "Daily Bonus", "Claim Now!", "gold", Modifier.weight(1f)) {}
            ActionCard("🏆", "Leaderboard", "Rank #23", "purple", Modifier.weight(1f)) {}
        }
    }
}

@Composable
fun ActionCard(icon: String, title: String, sub: String, glowType: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val glowColor = if (glowType == "gold") VaultGold else VaultPurple
    
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = glowColor.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(VaultCardLight, VaultCard)))
            .border(1.dp, glowColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Text(icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = VaultWhite, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(sub, color = glowColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RecentActivityList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(VaultCardLight, VaultCard)))
            .border(1.dp, VaultPurple.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            ActivityItem("✅", "Task Complete", "App Install", "+400", true, hasBorder = true)
            ActivityItem("🎰", "Mystery Box", "Daily Free", "+175", true, hasBorder = true)
            ActivityItem("📦", "Views Delivered", "1,000 views", "✓", true, hasBorder = false)
        }
    }
}

@Composable
fun ActivityItem(icon: String, title: String, desc: String, amount: String, isPositive: Boolean, hasBorder: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (hasBorder) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.04f),
                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(VaultGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = VaultWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(desc, color = VaultGrey, fontSize = 11.sp)
            }
        }
        
        Text(
            text = if (amount.startsWith("+")) "⚡ $amount" else amount,
            color = VaultGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun TagItem(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.13f), RoundedCornerShape(20.dp))
            .border(1.dp, color.copy(alpha = 0.27f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CustomProgressBar(progress: Float, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White.copy(alpha = 0.07f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(5.dp))
                .background(Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.67f))))
        )
    }
}
