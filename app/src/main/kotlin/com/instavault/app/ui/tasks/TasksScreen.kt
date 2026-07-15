package com.instavault.app.ui.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.home.TagItem
import com.instavault.app.ui.theme.*

data class TaskData(
    val icon: String,
    val title: String,
    val desc: String,
    val time: String,
    val type: String
)

@Composable
fun TasksScreen(
    onNavigateToSuccess: () -> Unit = {}
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    
    val tasks = listOf(
        TaskData("📱", "App Install", "\"FunBuzz\" install karo + register karo", "~3 min", "app_install"),
        TaskData("📋", "Quick Survey", "3 sawal ka jawab do", "~2 min", "survey"),
        TaskData("🌐", "Free Registration", "Website pe free account banao", "~4 min", "register"),
        TaskData("🎬", "Watch & Answer", "Short video + 2 questions", "~3 min", "video"),
        TaskData("📝", "Form Fill", "Basic form fill karo", "~2 min", "form")
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
                text = "⚡ Aajka Task",
                color = VaultWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
                    .background(VaultGold.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .border(1.dp, VaultGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎯", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Koi bhi 1 task → ⚡ 400 Sparks guaranteed!",
                        color = VaultGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        items(tasks.size) { index ->
            TaskItem(
                task = tasks[index],
                index = index,
                isSelected = selectedIndex == index,
                onSelect = { 
                    selectedIndex = if (selectedIndex == index) null else index 
                },
                onStartTask = onNavigateToSuccess
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TaskItem(
    task: TaskData,
    index: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStartTask: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) VaultPurple else Color.White.copy(alpha = 0.05f),
        animationSpec = tween(200)
    )
    
    val shadowSize = if (isSelected) 12.dp else 0.dp
    val shadowColor = if (isSelected) VaultPurple.copy(alpha = 0.25f) else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(shadowSize, RoundedCornerShape(18.dp), spotColor = shadowColor)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isSelected) {
                    Brush.linearGradient(listOf(VaultPurple.copy(alpha = 0.3f), VaultCard))
                } else {
                    Brush.linearGradient(listOf(VaultCardLight, VaultCard))
                }
            )
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // Icon Box
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VaultPurple.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(task.icon, fontSize = 22.sp)
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Task Details
                    Column {
                        Text(
                            "${index + 1}. ${task.title}",
                            color = VaultWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                        Text(
                            task.desc,
                            color = VaultGrey,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TagItem("⏱️ ${task.time}", VaultGreyLight)
                            TagItem("⚡ 400", VaultGold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Radio/Check circle
                val checkColor = if (isSelected) VaultPurple else VaultGrey
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .border(2.dp, checkColor, CircleShape)
                        .background(if (isSelected) VaultPurple else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            // Expandable Action Button
            AnimatedVisibility(
                visible = isSelected,
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(200))
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = VaultPurple.copy(alpha = 0.55f))
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                            .clickable { onStartTask() }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "▶️ Start Karo — ⚡ 400 Sparks Kamao",
                            color = VaultWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
