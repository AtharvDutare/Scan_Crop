package com.example.trysample.ui.screens

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trysample.R
import com.example.trysample.ui.theme.HealthyGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trysample.auth.AuthViewModel
import com.google.firebase.auth.FirebaseUser

private const val TAG = "ProfileScreen"

/**
 * ProfileScreen composable that displays the user's profile information and settings.
 * 
 * @param onSignOut Callback function that is invoked when the user clicks the sign out button.
 * @param modifier Optional modifier for customizing the layout of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get the AuthViewModel to access user information
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Debug log to check user information
    LaunchedEffect(currentUser) {
        Log.d(TAG, "Current user: ${currentUser?.uid}")
        Log.d(TAG, "Display name: ${currentUser?.displayName}")
        Log.d(TAG, "Email: ${currentUser?.email}")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CropScan",
                    style = MaterialTheme.typography.titleLarge
                )
                // Sign out button
                IconButton(onClick = onSignOut) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Sign Out"
                    )
                }
            }
        }

        // Profile Info
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image with first letter of name
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val displayName = currentUser?.displayName
                        if (displayName.isNullOrEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = displayName.first().toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Profile Details
                    Column {
                        val displayName = currentUser?.displayName
                        val email = currentUser?.email
                        
                        Text(
                            text = when {
                                !displayName.isNullOrEmpty() -> displayName
                                !email.isNullOrEmpty() -> email.substringBefore('@')
                                else -> "User"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (!email.isNullOrEmpty()) {
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Farm Owner",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Statistics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticCard(
                    value = "245",
                    label = "Acres",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatisticCard(
                    value = "38",
                    label = "Scans",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatisticCard(
                    value = "92%",
                    label = "Success",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Disease Reports
        item {
            Text(
                text = "Recent Disease Reports",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Disease Report Items
        items(sampleDiseaseReports) { report ->
            DiseaseReportCard(report = report)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatisticCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DiseaseReportCard(
    report: DiseaseReport,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Disease Image
            Image(
                painter = painterResource(
                    when (report.type) {
                        DiseaseType.CORN_LEAF_BLIGHT -> R.drawable.ic_corn_disease
                        DiseaseType.SOYBEAN_BLIGHT -> R.drawable.ic_soybean_disease
                    }
                ),
                contentDescription = report.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Disease Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = report.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Detected on ${report.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Status Badge
            Surface(
                color = when (report.status) {
                    DiseaseStatus.ACTIVE -> Color(0xFFFFF3E0)
                    DiseaseStatus.TREATED -> HealthyGreen.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = report.status.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = when (report.status) {
                        DiseaseStatus.ACTIVE -> Color(0xFFF57C00)
                        DiseaseStatus.TREATED -> HealthyGreen
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

enum class DiseaseType {
    CORN_LEAF_BLIGHT,
    SOYBEAN_BLIGHT
}

enum class DiseaseStatus {
    ACTIVE,
    TREATED
}

data class DiseaseReport(
    val name: String,
    val date: String,
    val type: DiseaseType,
    val status: DiseaseStatus
)

private val sampleDiseaseReports = listOf(
    DiseaseReport(
        name = "Northern Corn Leaf Blight",
        date = "May 15, 2024",
        type = DiseaseType.CORN_LEAF_BLIGHT,
        status = DiseaseStatus.ACTIVE
    ),
    DiseaseReport(
        name = "Soybean Bacterial Blight",
        date = "May 12, 2024",
        type = DiseaseType.SOYBEAN_BLIGHT,
        status = DiseaseStatus.TREATED
    )
) 