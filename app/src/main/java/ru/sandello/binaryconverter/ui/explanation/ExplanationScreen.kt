package ru.sandello.binaryconverter.ui.explanation

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.ui.components.RadixOutlinedTextField
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun ExplanationScreen(
    viewModel: ExplanationViewModel,
) {
    Column {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var customRadix1Expanded by remember { mutableStateOf(false) }
            var customRadix2Expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = customRadix1Expanded,
                onExpandedChange = {
                    customRadix1Expanded = !customRadix1Expanded
                },
                modifier = Modifier.weight(1f),
            ) {
                RadixOutlinedTextField(
                    value = viewModel.nsFrom.value.radix.value.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = customRadix1Expanded
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                )
                ExposedDropdownMenu(
                    expanded = customRadix1Expanded,
                    onDismissRequest = {
                        customRadix1Expanded = false
                    },
                ) {
                    viewModel.radixes.forEach { radix ->
                        DropdownMenuItem(
                            text = {
                                Text(text = radix.value.toString())
                            },
                            onClick = {
                                viewModel.updateRadix(radixType = RadixType.RadixCustom1, newRadix = radix)
                                customRadix1Expanded = false
                            },
                        )
                    }
                }
            }
            var atEnd by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
                    viewModel.radixesViceVersa()
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
            ExposedDropdownMenuBox(
                expanded = customRadix2Expanded,
                onExpandedChange = {
                    customRadix2Expanded = !customRadix2Expanded
                },
                modifier = Modifier.weight(1f),
            ) {
                RadixOutlinedTextField(
                    value = viewModel.nsTo.value.radix.value.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = customRadix2Expanded
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                )
                ExposedDropdownMenu(
                    expanded = customRadix2Expanded,
                    onDismissRequest = {
                        customRadix2Expanded = false
                    },
                ) {
                    viewModel.radixes.forEach { radix ->
                        DropdownMenuItem(
                            text = {
                                Text(text = radix.value.toString())
                            },
                            onClick = {
                                viewModel.updateRadix(radixType = RadixType.RadixCustom2, newRadix = radix)
                                customRadix2Expanded = false
                            },
                        )
                    }
                }
            }
        }
        AnimatedContent(
            targetState = viewModel.explanationState.collectAsState().value,
        ) { targetExpanded ->
            when (targetExpanded) {
                ExplanationState.Calculating -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .navigationBarsPadding()
                        )
                    }
                }
                is ExplanationState.Complete -> {
                    ExplanationContent(from = targetExpanded.from, to = targetExpanded.to)
                }
            }
        }
    }
}

@Composable
@Preview(name = "ExplanationScreen", group = "Calculating")
private fun PreviewExplanationCalculating() {
    NumberSystemsTheme {
        Surface {
            ExplanationScreen(viewModel = ExplanationViewModel())
        }
    }
}

@Composable
@Preview(name = "Explanation Calculating Dark", group = "Calculating")
private fun PreviewExplanationCalculatingDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(viewModel = ExplanationViewModel())
        }
    }
}
