package com.instavault.app.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.theme.*

data class GameData(
    val icon: String,
    val title: String,
    val sub: String,
    val reward: String,
    val color: Color,
    val id: String
)

@Composable
fun GameScreen(
    onNavigateToGame: (String) -> Unit = {}
) {
    val games = listOf(
        GameData("🧠", "Quiz Master", "GK + Bollywood", "+30 Sparks", VaultBlue, "quiz"),
        GameData("🎯", "Tap Frenzy", "30 sec challenge", "+20 Sparks", VaultGreen, "tap"),
        GameData("🎰", "Spin Wheel", "Daily free spin!", "Up to +500 Sparks", VaultGold, "spin"),
        GameData("🃏", "Card Match", "Memory game", "+40 Sparks", VaultPurple, "cards"),
        GameData("🔢", "Math Rush", "Quick calculations", "+25 Sparks", Color(0xFFFF6B9D), "math"),
        GameData("🔤", "Word Hunt", "Find hidden words", "+35 Sparks", Color(0xFF00CEC9), "words")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBg)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 90.dp) // Bottom padding for navigation
    ) {
        Text(
            text = "🎮 Games",
            color = VaultWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Task ke baad bonus Sparks kamao!",
            color = VaultGrey,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(games) { game ->
                GameCard(game = game, onClick = { onNavigateToGame(game.id) })
            }
        }
    }
}

@Composable
fun GameCard(game: GameData, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = game.color.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(game.color.copy(alpha = 0.18f), VaultCard)))
            .border(1.dp, game.color.copy(alpha = 0.33f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 18.dp)
    ) {
        Column {
            Text(
                text = game.icon,
                fontSize = 34.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    // We simulate drop shadow on text in compose using style or omitting it if native emoji doesn't support it well.
                    // For simplicity, native emoji renders fine.
            )
            Text(
                text = game.title,
                color = VaultWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = game.sub,
                color = VaultGrey,
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Box(
                modifier = Modifier
                    .background(game.color.copy(alpha = 0.22f), RoundedCornerShape(8.dp))
                    .border(1.dp, game.color.copy(alpha = 0.44f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = game.reward,
                    color = game.color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
