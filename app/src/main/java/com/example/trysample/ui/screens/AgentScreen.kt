package com.example.trysample.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
import com.example.trysample.R
import com.example.trysample.ui.theme.HealthyGreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trysample.navigation.BottomNavItem
//import com.example.trysample.weather.WeatherViewModel
import com.example.trysample.weatherpart.WeatherViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trysample.data.Scan
import com.example.trysample.data.AgentDataSource.recentScans

/**
 * AgentScreen composable that displays the AI agent interface for crop scanning and analysis.
 * 
 * @param modifier Optional modifier for customizing the layout of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    // Get the WeatherViewModel to access weather information
    val weatherViewModel: WeatherViewModel = viewModel()
    //val weatherState by weatherViewModel.weatherState.collectAsState()
    
    var showScanDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showScanDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Scan Plant",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Agent List",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Weather info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.WbSunny,
//                            contentDescription = "Weather",
//                            tint = Color(0xFFFFC107)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = "${""}°C",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Recent Scan Items
            items(recentScans) { scan ->
                ScanCard(
                    scan = scan,
                    onViewDetailsClick = {
                        navController.navigate("agent_detail/${scan.id}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Scan Dialog
        if (showScanDialog) {
            ScanDialog(
                onDismiss = { showScanDialog = false },
                onScanComplete = { /* TODO: Implement scan completion */ }
            )
        }
    }
}

@Composable
fun ScanCard(
    scan: Scan,
    modifier: Modifier = Modifier,
    onViewDetailsClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Time Posted at top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = scan.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Agent Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Agent Photo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                // Agent Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Agent Information",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Agent Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = scan.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Number of laborers
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Laborers: ${scan.laborers}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Per labor charge
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Per Labor: $${scan.laborCharge}/day",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Total cost
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Total: $${scan.laborers * scan.laborCharge}/day",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewDetailsClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HealthyGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Details")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDialog(
    onDismiss: () -> Unit,
    onScanComplete: (Scan) -> Unit
) {
    var isScanning by remember { mutableStateOf(false) }
    var scanProgress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Scan Plant",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isScanning) {
                    Text(
                        text = "Analyzing plant health...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    LinearProgressIndicator(
                        progress = scanProgress,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Simulate scanning progress
                    LaunchedEffect(isScanning) {
                        scope.launch {
                            for (i in 0..100 step 5) {
                                scanProgress = i / 100f
                                delay(100)
                            }
                            
                            // Create a sample scan result
                            val scanResult = Scan(
                                id = System.currentTimeMillis().toString(),
                                name = "Corn Field",
                                description = "No signs of disease detected. Plant appears healthy with good leaf color and structure.",
                                status = "Healthy",
                                confidence = 92,
                                date = "Today",
                                imageResId = R.drawable.corn_field,
                                laborers = 5,
                                laborCharge = 20
                            )
                            
                            onScanComplete(scanResult)
                            onDismiss()
                        }
                    }
                } else {
                    Text(
                        text = "Position your camera to scan a plant",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!isScanning) {
                Button(
                    onClick = { isScanning = true }
                ) {
                    Text("Start Scan")
                }
            }
        },
        dismissButton = {
            if (!isScanning) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
} 