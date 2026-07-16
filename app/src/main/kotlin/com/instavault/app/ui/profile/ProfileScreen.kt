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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.R
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
            ProfileHeaderCard()
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LinkedInstagramCard()
            Spacer(modifier = Modifier.height(16.dp))
            OrderViewsCTA()
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
            SettingsSection()
        }
    }
}

@Composable
fun ProfileHeaderCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        // Cover Image Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(VaultPurple.copy(alpha = 0.5f), VaultCard)))
                .border(1.dp, VaultPurple.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderStat(value = "2,470", label = "Balance")
                Box(modifier = Modifier.height(24.dp).width(1.dp).background(Color.White.copy(alpha = 0.15f)))
                HeaderStat(value = "12,840", label = "Earned")
                Box(modifier = Modifier.height(24.dp).width(1.dp).background(Color.White.copy(alpha = 0.15f)))
                HeaderStat(value = "#128", label = "Rank")
                Box(modifier = Modifier.height(24.dp).width(1.dp).background(Color.White.copy(alpha = 0.15f)))
                HeaderStat(value = "5🔥", label = "Streak")
            }
        }
        
        // Avatar and Info overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 84.dp), // Overlaps the cover (130 - 46 = 84)
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Ring Cutout
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(VaultBg) // Matches screen background for the cutout effect
                    .padding(4.dp)
                    .border(2.dp, VaultPurpleLight, CircleShape)
                    .padding(3.dp)
            ) {
                // Actual Avatar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(VaultGold, VaultPurple))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("RA", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("Rahul", color = VaultWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text("@rahul_vault • #VLT-00001", color = VaultGrey, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(14.dp))
            TagItem("🥉 Rookie Vaulter", VaultGold)
        }
    }
}

@Composable
fun HeaderStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = VaultWhite, fontSize = 15.sp, fontWeight = FontWeight.Black)
        Text(label, color = VaultGrey, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
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
fun SettingsSection() {
    Column {
        Text(
            text = "SETTINGS",
            color = VaultGrey,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VaultCardLight)
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
        ) {
            SettingRow("🔔", "Notifications", isFirst = true)
            SettingRow("🌐", "Language — Hinglish")
            SettingRow("ℹ️", "Help & Support")
            SettingRow("🚪", "Logout", isLogout = true)
        }
    }
}

@Composable
fun SettingRow(icon: String, text: String, isFirst: Boolean = false, isLogout: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (!isFirst) Modifier.drawBehind {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                } else Modifier
            )
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text, 
            color = if (isLogout) VaultRed else VaultWhite, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.SemiBold, 
            modifier = Modifier.weight(1f)
        )
        Text("›", color = VaultGrey, fontSize = 20.sp)
    }
}

@Composable
fun LinkedInstagramCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VaultCardLight)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // IG Icon Box
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(VaultPurple.copy(alpha = 0.12f))
                .border(1.dp, VaultPurple.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(id = R.drawable.ic_instagram), contentDescription = "Instagram", tint = VaultPurple, modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text("Linked Instagram", color = VaultGrey, fontSize = 11.sp)
            Text("@achal.creates", color = VaultWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 2.dp))
        }
        
        // Verified Chip
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF3B82F6).copy(alpha = 0.12f))
                .border(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("✓", color = Color(0xFF3B82F6), fontSize = 10.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Verified", color = Color(0xFF3B82F6), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun OrderViewsCTA() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(VaultGold, VaultPurple)))
                .clickable { }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Order Views on Bot", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Sparks se views order karne bot par jao ↗", color = VaultGrey, fontSize = 11.5.sp)
    }
}
