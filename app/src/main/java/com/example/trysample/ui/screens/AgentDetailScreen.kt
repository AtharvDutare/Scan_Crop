package com.example.trysample.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trysample.ui.theme.HealthyGreen
import com.example.trysample.data.Scan
import com.example.trysample.data.AgentDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentDetailScreen(
    navController: NavController,
    agentId: String? = null
) {
    val agent = getScanById(agentId)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Farm Labour Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement sharing */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Profile Header with Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    // Background image placeholder (in a real app, you'd load the actual image)
                    // Using a colored background that looks like the worker in blue hard hat
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF7D9EC0))
                    )
                    
                    // Darkening overlay for better text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f))
                    )
                    
                    // Name and rating overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = agent?.name ?: "John Farmer",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rating stars
                            Row {
                                repeat(4) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.StarHalf,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            Text(
                                text = "4.8 (124 reviews)",
                                color = Color.White
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Verified badge
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Verified",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Rate and Success Rate
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$${agent?.laborCharge ?: 25}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Daily Rate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.LightGray
                    )
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text ="$${agent?.laborers ?: 25}" ,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "laborers",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
            
            // Availability Section
//            item {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        text = "Availability",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Calendar row
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        DayAvailability(day = "Mon", date = "24", isAvailable = true)
//                        DayAvailability(day = "Tue", date = "25", isAvailable = true)
//                        DayAvailability(day = "Wed", date = "26", isAvailable = false, isBooked = true)
//                        DayAvailability(day = "Thu", date = "27", isAvailable = true)
//                        DayAvailability(day = "Fri", date = "28", isAvailable = true)
//                    }
//                }
//
//                Divider(modifier = Modifier.padding(horizontal = 16.dp))
//            }
            
            // Recent Reviews
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recent Reviews",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Review 1
                    ReviewItem(
                        name = "Sarah Mitchell",
                        rating = 5f,
                        timeAgo = "2 days ago",
                        comment = "Excellent work! Robert was professional, punctual, and completed the job perfectly. Would definitely recommend!"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Review 2
                    ReviewItem(
                        name = "Michael Chen",
                        rating = 4.5f,
                        timeAgo = "1 week ago",
                        comment = "Very skilled and efficient. Completed a complex plumbing job in no time. Fair pricing too."
                    )
                }
            }
            
            // Action Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Implement call now */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call Now")
                    }
                    
//                    Button(
//                        onClick = { /* TODO: Implement book now */ },
//                        modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = HealthyGreen
//                        )
//                    ) {
//                        Text("Book Now")
//                    }
                }
            }
        }
    }
}

@Composable
fun DayAvailability(
    day: String,
    date: String,
    isAvailable: Boolean,
    isBooked: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isBooked -> Color(0xFFFEE2E2)
                    isAvailable -> Color(0xFFECFDF5)
                    else -> Color(0xFFF3F4F6)
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = when {
                isBooked -> "Booked"
                isAvailable -> "Available"
                else -> "Unavailable"
            },
            style = MaterialTheme.typography.bodySmall,
            color = when {
                isBooked -> Color.Red
                isAvailable -> HealthyGreen
                else -> Color.Gray
            }
        )
    }
}

@Composable
fun ReviewItem(
    name: String,
    rating: Float,
    timeAgo: String,
    comment: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Reviewer avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            // Display first letter of name
            Text(
                text = name.firstOrNull()?.toString() ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            // Rating stars
            Row {
                val fullStars = rating.toInt()
                val hasHalfStar = rating - fullStars > 0
                
                repeat(fullStars) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                if (hasHalfStar) {
                    Icon(
                        imageVector = Icons.Default.StarHalf,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Helper function to get a scan by ID
private fun getScanById(id: String?): Scan? {
    if (id == null) return null
    return AgentDataSource.recentScans.find { it.id == id }
}