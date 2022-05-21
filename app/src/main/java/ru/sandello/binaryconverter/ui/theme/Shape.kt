package ru.sandello.binaryconverter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(36.dp),
)

val ShapesTop = Shapes(
    small = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    medium = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    large = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
)
