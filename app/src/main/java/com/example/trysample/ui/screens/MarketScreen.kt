package com.example.trysample.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.trysample.ui.theme.HealthyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    modifier: Modifier = Modifier
) {
    var showAddPostDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPostDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Post",
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
                        text = "Crop Marketplace",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Filter chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = true,
                        onClick = { /* TODO: Implement filter */ },
                        label = { Text("All") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                    FilterChip(
                        selected = false,
                        onClick = { /* TODO: Implement filter */ },
                        label = { Text("Corn") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Grass,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                    FilterChip(
                        selected = false,
                        onClick = { /* TODO: Implement filter */ },
                        label = { Text("Soybeans") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Market Posts
            items(sampleMarketPosts) { post ->
                MarketPostCard(post = post)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Add Post Dialog
        if (showAddPostDialog) {
            AddPostDialog(
                onDismiss = { showAddPostDialog = false },
                onPostAdded = { newPost ->
                    // In a real app, you would add this to your data source
                    showAddPostDialog = false
                }
            )
        }
    }
}

@Composable
fun MarketPostCard(
    post: MarketPost,
    modifier: Modifier = Modifier
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
            // Post Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.sellerName.first().toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = post.sellerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = post.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Text(
                    text = post.price,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Post Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Crop Image
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (post.cropType) {
                            CropType.CORN -> Icons.Default.Grass
                            CropType.SOYBEAN -> Icons.Default.Spa
                            CropType.WHEAT -> Icons.Default.Grain
                        },
                        contentDescription = post.cropName,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                // Post Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = post.cropName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quantity: ${post.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Text(
                            text = post.datePosted,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    onClick = { /* TODO: Implement buy */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HealthyGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buy Now")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostDialog(
    onDismiss: () -> Unit,
    onPostAdded: (MarketPost) -> Unit
) {
    var cropName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCropType by remember { mutableStateOf(CropType.CORN) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Post",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Crop Type Selection
                Text(
                    text = "Crop Type",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CropType.values().forEach { cropType ->
                        FilterChip(
                            selected = selectedCropType == cropType,
                            onClick = { selectedCropType = cropType },
                            label = {
                                Text(
                                    when (cropType) {
                                        CropType.CORN -> "Corn"
                                        CropType.SOYBEAN -> "Soybean"
                                        CropType.WHEAT -> "Wheat"
                                    }
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (cropType) {
                                        CropType.CORN -> Icons.Default.Grass
                                        CropType.SOYBEAN -> Icons.Default.Spa
                                        CropType.WHEAT -> Icons.Default.Grain
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Crop Name
                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text("Crop Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                // Quantity
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                // Price
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newPost = MarketPost(
                        id = System.currentTimeMillis().toString(),
                        cropName = cropName,
                        cropType = selectedCropType,
                        description = description,
                        quantity = quantity.toIntOrNull() ?: 0,
                        price = "$$price",
                        sellerName = "John Farmer", // In a real app, this would come from the user profile
                        location = "Iowa, USA",
                        datePosted = "Today"
                    )
                    onPostAdded(newPost)
                },
                enabled = cropName.isNotBlank() && description.isNotBlank() && 
                          quantity.isNotBlank() && price.isNotBlank()
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

enum class CropType {
    CORN,
    SOYBEAN,
    WHEAT
}

data class MarketPost(
    val id: String,
    val cropName: String,
    val cropType: CropType,
    val description: String,
    val quantity: Int,
    val price: String,
    val sellerName: String,
    val location: String,
    val datePosted: String
)

private val sampleMarketPosts = listOf(
    MarketPost(
        id = "1",
        cropName = "Yellow Corn",
        cropType = CropType.CORN,
        description = "Freshly harvested yellow corn, non-GMO, perfect for feed or processing. Available in bulk quantities.",
        quantity = 5000,
        price = "$3.50/bushel",
        sellerName = "John Farmer",
        location = "Iowa, USA",
        datePosted = "2 hours ago"
    ),
    MarketPost(
        id = "2",
        cropName = "Soybeans",
        cropType = CropType.SOYBEAN,
        description = "High-quality soybeans with 35% protein content. Suitable for food processing and animal feed.",
        quantity = 3000,
        price = "$12.75/bushel",
        sellerName = "Sarah Fields",
        location = "Illinois, USA",
        datePosted = "5 hours ago"
    ),
    MarketPost(
        id = "3",
        cropName = "Winter Wheat",
        cropType = CropType.WHEAT,
        description = "Premium winter wheat, 13% protein content. Ready for harvest in 2 weeks. Pre-orders welcome.",
        quantity = 2000,
        price = "$7.25/bushel",
        sellerName = "Mike Grains",
        location = "Kansas, USA",
        datePosted = "1 day ago"
    )
) 