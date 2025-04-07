package com.example.trysample.data

import com.example.trysample.R

data class Scan(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val confidence: Int,
    val date: String,
    val imageResId: Int,
    val laborers: Int = 5,
    val laborCharge: Int = 20
)

object AgentDataSource {
    val recentScans = listOf(
        Scan(
            id = "1",
            name = "John Smith",
            description = "No signs of disease detected. Plant appears healthy with good leaf color and structure.",
            status = "Healthy",
            confidence = 92,
            date = "2 hours ago",
            imageResId = R.drawable.corn_field,
            laborers = 5,
            laborCharge = 20
        ),
        Scan(
            id = "2",
            name = "Maria Garcia",
            description = "Minor signs of leaf spot disease detected. Consider applying fungicide treatment.",
            status = "Warning",
            confidence = 85,
            date = "5 hours ago",
            imageResId = R.drawable.soybean_field,
            laborers = 3,
            laborCharge = 25
        ),
        Scan(
            id = "3",
            name = "David Chen",
            description = "No signs of disease detected. Plant appears healthy with good growth pattern.",
            status = "Healthy",
            confidence = 95,
            date = "1 day ago",
            imageResId = R.drawable.wheat_field,
            laborers = 4,
            laborCharge = 22
        )
    )
} 