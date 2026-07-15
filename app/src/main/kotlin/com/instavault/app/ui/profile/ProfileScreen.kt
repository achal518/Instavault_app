package com.instavault.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.home.TagItem
import com.instavault.app.ui.theme.*

data class BadgeData(
    val icon: String,
    val title: String,
    val isUnlocked: Boolean
)

data class StatData(
    val icon: String,
    val value: String,
    val label: String
)

@Composable
fun ProfileScreen() {
    val badges = listOf(
        BadgeData("🏅", "First Order", true),
        BadgeData("🔥", "3-Day", true),
        BadgeData("🔥", "7-Day", true),
        BadgeData("🤝", "First Ref", false),
        BadgeData("💎", "Elite", false),
        BadgeData("👑", "VaultKing", false)
    )

    val stats = listOf(
        StatData("🔥", "7 Days", "Streak"),
        StatData("👁️", "1,000", "Views"),
        StatData("⭐", "90", "RP"),
        StatData("🤝", "0", "Referrals")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        item {
            Text(
                text = "👤 My Vault",
                color = VaultWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        item {
            ProfileHeaderCard()
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            StatsGrid(stats = stats)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ACHIEVEMENTS",
                color = VaultGrey,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            AchievementsGrid(badges = badges)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            ActionButtons()
        }
    }
}

@Composable
fun ProfileHeaderCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(VaultPurple.copy(alpha = 0.27f), VaultCard)))
            .border(1.dp, VaultPurple.copy(alpha = 0.27f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                    .border(3.dp, VaultGold.copy(alpha = 0.27f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("RA", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column {
                Text("Rahul", color = VaultWhite, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(
                    "#VLT-00001 • Member since May 2025",
                    color = VaultGrey,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TagItem("🥉 Rookie Vaulter", VaultGold)
            }
        }
    }
}

@Composable
fun StatsGrid(stats: List<StatData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(stats[0], Modifier.weight(1f))
            StatCard(stats[1], Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(stats[2], Modifier.weight(1f))
            StatCard(stats[3], Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(stat: StatData, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(VaultCard)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stat.icon, fontSize = 26.sp, modifier = Modifier.padding(bottom = 4.dp))
            Text(stat.value, color = VaultWhite, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text(stat.label, color = VaultGrey, fontSize = 11.sp)
        }
    }
}

@Composable
fun AchievementsGrid(badges: List<BadgeData>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BadgeCard(badges[0], Modifier.weight(1f))
            BadgeCard(badges[1], Modifier.weight(1f))
            BadgeCard(badges[2], Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BadgeCard(badges[3], Modifier.weight(1f))
            BadgeCard(badges[4], Modifier.weight(1f))
            BadgeCard(badges[5], Modifier.weight(1f))
        }
    }
}

@Composable
fun BadgeCard(badge: BadgeData, modifier: Modifier = Modifier) {
    val bgColor = if (badge.isUnlocked) VaultGold.copy(alpha = 0.08f) else VaultCard
    val borderColor = if (badge.isUnlocked) VaultGold.copy(alpha = 0.27f) else Color.White.copy(alpha = 0.05f)
    val alpha = if (badge.isUnlocked) 1f else 0.4f

    Box(
        modifier = modifier
            .alpha(alpha)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Emulate grayscale for locked icons by using opacity or just relying on the box alpha
            // For true grayscale, we'd need a ColorMatrix, but alpha is close enough to JSX's intent
            Text(
                text = badge.icon,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = badge.title,
                color = if (badge.isUnlocked) VaultGold else VaultGrey,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            if (!badge.isUnlocked) {
                Text("🔒", fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
fun ActionButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ActionButton("📲", "Telegram Bot Pe Jaao")
        ActionButton("⚙️", "Settings")
    }
}

@Composable
fun ActionButton(icon: String, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VaultCardLight)
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = VaultWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Text("→", color = VaultGrey, fontSize = 16.sp)
    }
}
