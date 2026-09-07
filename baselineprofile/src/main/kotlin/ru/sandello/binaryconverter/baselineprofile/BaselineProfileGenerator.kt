package ru.sandello.binaryconverter.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = PACKAGE_NAME,
            includeInStartupProfile = true,
        ) {
            pressHome()
            startActivityAndWait()

            device.wait(Until.hasObject(By.res(PACKAGE_NAME, TAG_CONVERTER_DEC)), TIMEOUT_MS)
            device.findObject(By.res(PACKAGE_NAME, TAG_CONVERTER_DEC))?.text = "255"

            device.findObject(By.res(PACKAGE_NAME, TAG_CONVERTER_CUSTOM_RADIX))?.click()
            if (device.wait(Until.hasObject(By.text("12")), TIMEOUT_MS)) {
                device.findObject(By.text("12"))?.click()
            }

            device.findObject(By.res(PACKAGE_NAME, TAG_NAV_CALCULATOR))?.click()
            device.wait(Until.hasObject(By.res(PACKAGE_NAME, TAG_CALCULATOR_OPERAND_1)), TIMEOUT_MS)
            device.findObject(By.res(PACKAGE_NAME, TAG_CALCULATOR_OPERAND_1))?.text = "10"

            device.findObject(By.res(PACKAGE_NAME, TAG_NAV_SETTINGS))?.click()
            device.wait(Until.hasObject(By.res(PACKAGE_NAME, TAG_SETTINGS_LIST)), TIMEOUT_MS)
        }
    }

    private companion object {
        const val PACKAGE_NAME = "ru.sandello.binaryconverter"
        const val TIMEOUT_MS = 5_000L
        const val TAG_NAV_CALCULATOR = "nav_calculator"
        const val TAG_NAV_SETTINGS = "nav_settings"
        const val TAG_CONVERTER_DEC = "converter_dec"
        const val TAG_CONVERTER_CUSTOM_RADIX = "converter_custom_radix"
        const val TAG_CALCULATOR_OPERAND_1 = "calculator_operand_1"
        const val TAG_SETTINGS_LIST = "settings_list"
    }
}
