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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun ExplanationScreen(
    viewModel: ExplanationViewModel = viewModel(),
) {
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var customRadix1Expanded by remember { mutableStateOf(false) }
            var customRadix2Expanded by remember { mutableStateOf(false) }
            RadixExposedDropdown(
                expanded = customRadix1Expanded,
                onExpandedChange = { customRadix1Expanded = !customRadix1Expanded },
                onDismissRequest = { customRadix1Expanded = false },
                onRadixClicked = { radix ->
                    viewModel.updateRadix(radixType = RadixType.RadixCustom1, newRadix = radix)
                    customRadix1Expanded = false
                },
                radix = viewModel.nsFrom.value.radix,
                radixes = viewModel.radixes,
                modifier = Modifier.weight(1f),
                isCompact = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = customRadix1Expanded
                    )
                },
                shape = MaterialTheme.shapes.medium,
            )
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
            RadixExposedDropdown(
                expanded = customRadix2Expanded,
                onExpandedChange = { customRadix2Expanded = !customRadix2Expanded },
                onDismissRequest = { customRadix2Expanded = false },
                onRadixClicked = { radix ->
                    viewModel.updateRadix(radixType = RadixType.RadixCustom2, newRadix = radix)
                    customRadix2Expanded = false
                },
                radix = viewModel.nsTo.value.radix,
                radixes = viewModel.radixes,
                modifier = Modifier.weight(1f),
                isCompact = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = customRadix2Expanded
                    )
                },
                shape = MaterialTheme.shapes.medium,
            )
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
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
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
            ExplanationScreen()
        }
    }
}

@Composable
@Preview(name = "Explanation Calculating Dark", group = "Calculating")
private fun PreviewExplanationCalculatingDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen()
        }
    }
}
