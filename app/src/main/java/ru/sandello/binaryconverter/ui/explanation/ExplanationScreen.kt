package ru.sandello.binaryconverter.ui.explanation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
                OutlinedTextField(
                    value = viewModel.nsFrom.value.radix.value.toString(),
                    onValueChange = { },
                    readOnly = true,
                    label = {},
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
            IconButton(onClick = viewModel::radixesViceVersa) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_shuffle_24), contentDescription = null)
            }
            ExposedDropdownMenuBox(
                expanded = customRadix2Expanded,
                onExpandedChange = {
                    customRadix2Expanded = !customRadix2Expanded
                },
                modifier = Modifier.weight(1f),
            ) {
                OutlinedTextField(
                    value = viewModel.nsTo.value.radix.value.toString(),
                    onValueChange = { },
                    readOnly = true,
                    label = {},
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
