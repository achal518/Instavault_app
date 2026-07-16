package com.instavault.app.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instavault.app.ui.theme.*

data class TaskData(
    val title: String,
    val provider: String,
    val reward: String,
    val duration: String,
    val type: String
)

@Composable
fun TasksScreen(
    onNavigateToSuccess: () -> Unit = {}
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    
    val tasks = listOf(
        TaskData("Install 'FunBuzz' App", "AdGem", "400", "3 min", "App Install"),
        TaskData("Complete Quick Survey", "CPALead", "350", "5 min", "Survey"),
        TaskData("Create Free Account", "AdGate", "500", "4 min", "Registration"),
        TaskData("Watch Video & Answer", "OfferToro", "300", "2 min", "Video"),
        TaskData("Fill Basic Profile Form", "BitLabs", "450", "3 min", "Form")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(VaultBg),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        // Futuristic Header
        item {
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Text(
                    text = "DAILY MISSIONS",
                    color = VaultPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Select Your Task",
                    color = VaultWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You can only complete 1 task per day from our CPA network partners. Choose wisely.",
                    color = VaultGrey,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                
                // Progress Bar
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daily Limit", color = VaultWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("0/1 Completed", color = VaultGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = VaultGold,
                    trackColor = VaultCardLight
                )
            }
        }
        
        items(tasks.size) { index ->
            TaskItemNeo(
                task = tasks[index],
                isSelected = selectedIndex == index,
                onSelect = { selectedIndex = if (selectedIndex == index) null else index },
                onStartTask = onNavigateToSuccess
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
fun TaskItemNeo(
    task: TaskData,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStartTask: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) VaultPurple else Color.White.copy(alpha = 0.08f),
        animationSpec = tween(300)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) VaultPurple.copy(alpha = 0.05f) else VaultCard)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onSelect() }
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Task Type Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VaultCardLight)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(task.type.uppercase(), color = VaultGrey, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
                
                // Reward Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(VaultGold.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Filled.FlashOn, contentDescription = "Sparks", tint = VaultGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(task.reward, color = VaultGold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            Text(
                task.title,
                color = VaultWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Provider: ${task.provider}", color = VaultGrey, fontSize = 12.sp)
                Text("  •  ", color = VaultGrey.copy(alpha = 0.5f), fontSize = 12.sp)
                Text("Duration: ${task.duration}", color = VaultGrey, fontSize = 12.sp)
            }
            
            AnimatedVisibility(
                visible = isSelected,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(300)) + fadeOut(tween(300))
            ) {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(VaultPurple, VaultPurpleLight)))
                            .clickable { onStartTask() }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "BEGIN MISSION",
                            color = VaultWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
