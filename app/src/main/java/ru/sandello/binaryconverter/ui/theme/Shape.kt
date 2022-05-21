package ru.sandello.binaryconverter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

val ShapesTop = Shapes(
    extraSmall = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
    small = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    medium = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    large = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    extraLarge = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
)
