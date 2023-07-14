package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun ExplanationScreen(
    explanationUiState: ExplanationUiState,
    onRadixChanged: (ExplanationRadixType, Radix) -> Unit,
) {
    AnimatedContent(
        targetState = explanationUiState.state,
        modifier = Modifier.animateContentSize(),
    ) { state ->
        when (state) {
            ExplanationState.Calculating -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

            is ExplanationState.Completed -> {
                Column(
                    modifier = Modifier
                        .padding(
                            start = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateStartPadding(LocalLayoutDirection.current),
                            end = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current),
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        var customRadix1Expanded by remember { mutableStateOf(false) }
                        var customRadix2Expanded by remember { mutableStateOf(false) }
                        RadixExposedDropdown(
                            expanded = customRadix1Expanded,
                            onExpandedChange = { customRadix1Expanded = !customRadix1Expanded },
                            onDismissRequest = { customRadix1Expanded = false },
                            onRadixClicked = { radix ->
                                onRadixChanged.invoke(ExplanationRadixType.RadixCustom1, radix)
                                customRadix1Expanded = false
                            },
                            radix = explanationUiState.from.radix,
                            radixes = explanationUiState.radixes,
                            modifier = Modifier.weight(1f, fill = false),
                            isCompact = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = customRadix1Expanded)
                            },
                            shape = MaterialTheme.shapes.medium,
                        )
                        var atEnd by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = {
                                onRadixChanged.invoke(ExplanationRadixType.RadixCustom1, explanationUiState.to.radix)
                                onRadixChanged.invoke(ExplanationRadixType.RadixCustom2, explanationUiState.from.radix)
                                atEnd = !atEnd
                            },
                        ) {
                            Icon(
                                painter = rememberAnimatedVectorPainter(
                                    AnimatedImageVector.animatedVectorResource(id = R.drawable.rotate),
                                    atEnd = atEnd,
                                ),
                                contentDescription = null,
                            )
                        }
                        RadixExposedDropdown(
                            expanded = customRadix2Expanded,
                            onExpandedChange = { customRadix2Expanded = !customRadix2Expanded },
                            onDismissRequest = { customRadix2Expanded = false },
                            onRadixClicked = { radix ->
                                onRadixChanged(ExplanationRadixType.RadixCustom2, radix)
                                customRadix2Expanded = false
                            },
                            radix = explanationUiState.to.radix,
                            radixes = explanationUiState.radixes,
                            modifier = Modifier.weight(1f, fill = false),
                            isCompact = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = customRadix2Expanded)
                            },
                            shape = MaterialTheme.shapes.medium,
                        )
                    }
                    ExplanationContent(from = explanationUiState.from, to = explanationUiState.to)
                }
            }
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen", group = "Completed")
private fun PreviewExplanationCompleted() {
    val explanationUiState = ExplanationUiState(
        state = ExplanationState.Completed,
        from = NumberSystem("10", Radix.BIN),
        to = NumberSystem("2", Radix.DEC),
    )

    NumberSystemsTheme {
        Surface {
            ExplanationScreen(
                explanationUiState = explanationUiState,
                onRadixChanged = { _, _ -> },
            )
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen Dark", group = "Completed")
private fun PreviewExplanationCompletedDark() {
    val explanationUiState = ExplanationUiState(
        state = ExplanationState.Completed,
        from = NumberSystem("10", Radix.BIN),
        to = NumberSystem("2", Radix.DEC),
    )

    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(
                explanationUiState = explanationUiState,
                onRadixChanged = { _, _ -> },
            )
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen", group = "Calculating")
private fun PreviewExplanationCalculating() {
    NumberSystemsTheme {
        Surface {
            ExplanationScreen(
                explanationUiState = ExplanationUiState(state = ExplanationState.Calculating),
                onRadixChanged = { _, _ -> },
            )
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen Dark", group = "Calculating")
private fun PreviewExplanationCalculatingDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(
                explanationUiState = ExplanationUiState(state = ExplanationState.Calculating),
                onRadixChanged = { _, _ -> },
            )
        }
    }
}

