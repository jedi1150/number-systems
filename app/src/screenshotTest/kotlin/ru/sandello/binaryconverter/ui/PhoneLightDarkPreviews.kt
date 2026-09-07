package ru.sandello.binaryconverter.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private const val PHONE = "spec:width=411dp,height=891dp"

@Preview(
    name = "light",
    showBackground = true,
    device = PHONE,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark",
    showBackground = true,
    device = PHONE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
internal annotation class PhoneLightDarkPreviews
