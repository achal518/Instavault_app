package com.instavault.app.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.theme.*

data class GameData(
    val title: String,
    val sub: String,
    val icon: String,
    val reward: String,
    val color: Color,
    val id: String
)

@Composable
fun GameScreen(
    onNavigateToGame: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBg)
    ) {
        // Futuristic Header
        Column(modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)) {
            Text(
                text = "ARCADE ZONE", 
                color = VaultPurple, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Black, 
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Play & Earn", 
                color = VaultWhite, 
                fontSize = 28.sp, 
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Complete your daily tasks to unlock arcade games. Multiply your Sparks easily!", 
                color = VaultGrey, 
                fontSize = 13.sp, 
                lineHeight = 18.sp
            )
        }
        
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                FeaturedGameCard(
                    title = "Daily Spin Wheel",
                    desc = "Test your luck every day. Win up to 500 Sparks instantly!",
                    icon = "🎰",
                    tag = "HOT",
                    color = VaultGold,
                    onClick = { onNavigateToGame("spin") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            val otherGames = listOf(
                GameData("Quiz Master", "Trivia challenges", "🧠", "Up to 50", VaultBlue, "quiz"),
                GameData("Predict & Win", "Crypto trends", "📈", "Up to 100", VaultGreen, "predict"),
                GameData("Tap Frenzy", "Fastest fingers", "⚡", "Up to 30", Color(0xFFFF6B9D), "tap")
            )
            
            items(otherGames) { game ->
                StandardGameCard(game = game, onClick = { onNavigateToGame(game.id) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FeaturedGameCard(title: String, desc: String, icon: String, tag: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(color.copy(alpha = 0.3f), VaultCard)))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        // Abstract glowing orb effect in the top right corner
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(Brush.radialGradient(listOf(color.copy(alpha = 0.2f), Color.Transparent)))
        )
        
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween, 
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f))
                        .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 26.sp)
                }
                
                // Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(tag, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(title, color = VaultWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(6.dp))
            Text(desc, color = VaultGrey, fontSize = 13.sp, lineHeight = 18.sp)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Play Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(color)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("PLAY NOW", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun StandardGameCard(game: GameData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VaultCard)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Game Icon
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(game.color.copy(alpha = 0.15f))
                .border(1.dp, game.color.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(game.icon, fontSize = 24.sp)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(game.title, color = VaultWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(game.sub, color = VaultGrey, fontSize = 12.sp)
        }
        
        // Reward pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(VaultCardLight)
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚡", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(game.reward, color = VaultWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
