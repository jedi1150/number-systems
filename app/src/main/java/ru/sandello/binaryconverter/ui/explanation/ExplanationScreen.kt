package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun ExplanationScreen(
    explanationUiState: ExplanationUiState,
    updateRadix: (ExplanationRadixType, Radix) -> Unit,
    swapRadixes: () -> Unit,
) {
    AnimatedContent(
        targetState = explanationUiState,
    ) { state ->
        when (state) {
            ExplanationUiState.Calculating -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }
            is ExplanationUiState.Complete -> {
                Column(
                    modifier = Modifier
                        .displayCutoutPadding()
                        .padding(
                            start = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateStartPadding(LocalLayoutDirection.current),
                            end = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current),
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.explanation),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                    )
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
                                updateRadix.invoke(ExplanationRadixType.RadixCustom1, radix)
                                customRadix1Expanded = false
                            },
                            radix = state.from.radix,
                            radixes = state.radixes,
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
                                swapRadixes()
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
                                updateRadix(ExplanationRadixType.RadixCustom2, radix)
                                customRadix2Expanded = false
                            },
                            radix = state.to.radix,
                            radixes = state.radixes,
                            modifier = Modifier.weight(1f, fill = false),
                            isCompact = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = customRadix2Expanded)
                            },
                            shape = MaterialTheme.shapes.medium,
                        )
                    }
                    ExplanationContent(from = state.from, to = state.to)
                }
            }
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen", group = "Complete")
private fun PreviewExplanationComplete() {
    val nsFrom = NumberSystem("10", Radix.BIN)
    val nsTo = NumberSystem("2", Radix.DEC)

    NumberSystemsTheme {
        Surface {
            ExplanationScreen(
                explanationUiState = ExplanationUiState.Complete(from = nsFrom, to = nsTo),
                updateRadix = { _, _ -> },
                swapRadixes = {},
            )
        }
    }
}

@SuppressLint("Range")
@Composable
@Preview(name = "ExplanationScreen Dark", group = "Complete")
private fun PreviewExplanationCompleteDark() {
    val nsFrom = NumberSystem("10", Radix.BIN)
    val nsTo = NumberSystem("2", Radix.DEC)

    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(
                explanationUiState = ExplanationUiState.Complete(from = nsFrom, to = nsTo),
                updateRadix = { _, _ -> },
                swapRadixes = {},
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
                explanationUiState = ExplanationUiState.Calculating,
                updateRadix = { _, _ -> },
                swapRadixes = {},
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
                explanationUiState = ExplanationUiState.Calculating,
                updateRadix = { _, _ -> },
                swapRadixes = {},
            )
        }
    }
}

