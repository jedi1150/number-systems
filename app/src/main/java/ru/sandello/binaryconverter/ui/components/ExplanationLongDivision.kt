package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ru.sandello.binaryconverter.model.LongDivision
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationLongDivision(longDivision: LongDivision) {

    fun decoupledDivideConstraints(): ConstraintSet {
        return ConstraintSet {
            val dividendRef = createRefFor("dividend")
            val divisionRef = createRefFor("division")
            val divisorRef = createRefFor("divisor")
            val quotientRef = createRefFor("quotient")
            val remainderRef = createRefFor("remainder")
            val subtractRef = createRefFor("subtract")
            val verticalLineDivisorRef = createRefFor("verticalLineDivisor")
            val horizontalLineRemainderRef = createRefFor("horizontalLineRemainder")
            val horizontalLineQuotientRef = createRefFor("horizontalLineQuotient")

            constrain(subtractRef) {
                start.linkTo(parent.start)
                linkTo(top = dividendRef.top, bottom = divisionRef.bottom)
            }

            constrain(verticalLineDivisorRef) {
                start.linkTo(horizontalLineRemainderRef.end)
                top.linkTo(parent.top)
                bottom.linkTo(horizontalLineRemainderRef.bottom)
                height = Dimension.fillToConstraints
            }

            constrain(horizontalLineQuotientRef) {
                top.linkTo(divisorRef.bottom)
                linkTo(start = verticalLineDivisorRef.end, end = parent.end)
                width = Dimension.fillToConstraints
            }

            constrain(horizontalLineRemainderRef) {
                top.linkTo(divisionRef.bottom)
                linkTo(start = parent.start, end = verticalLineDivisorRef.start)
                width = Dimension.fillToConstraints
            }

            constrain(dividendRef) {
                top.linkTo(parent.top)
                linkTo(
                    start = subtractRef.end,
                    end = verticalLineDivisorRef.start,
                    bias = 1f,
                    endMargin = 4.dp,
                )
            }

            constrain(divisionRef) {
                top.linkTo(dividendRef.bottom)
                linkTo(
                    start = subtractRef.end,
                    end = verticalLineDivisorRef.start,
                    bias = 1f,
                    endMargin = 4.dp,
                )
            }

            constrain(divisorRef) {
                start.linkTo(verticalLineDivisorRef.end, margin = 4.dp)
                top.linkTo(parent.top)
            }

            constrain(quotientRef) {
                start.linkTo(verticalLineDivisorRef.end, margin = 4.dp)
                top.linkTo(horizontalLineQuotientRef.bottom)
            }

            constrain(remainderRef) {
                top.linkTo(horizontalLineRemainderRef.bottom)
                linkTo(
                    start = parent.start,
                    end = verticalLineDivisorRef.start,
                    startMargin = 4.dp,
                    endMargin = 4.dp,
                    bias = 1f,
                )
            }
        }
    }

    ConstraintLayout(constraintSet = decoupledDivideConstraints()) {
        Text(text = longDivision.dividend.toString(), modifier = Modifier.layoutId("dividend"))
        Text(text = longDivision.divisor.toString(), modifier = Modifier.layoutId("divisor"))
        Text(text = longDivision.quotient.toString(), modifier = Modifier.layoutId("quotient"))
        Text(text = longDivision.division.toString(), modifier = Modifier.layoutId("division"))
        Text(text = longDivision.remainder.toString(), modifier = Modifier.layoutId("remainder"))
        Text(text = "-", modifier = Modifier.layoutId("subtract"))
        Divider(
            modifier = Modifier
                .width(width = 1.dp)
                .layoutId("verticalLineDivisor"),
        )
        Divider(
            modifier = Modifier
                .height(height = 1.dp)
                .layoutId("horizontalLineQuotient"),
        )
        Divider(
            modifier = Modifier
                .height(height = 1.dp)
                .layoutId("horizontalLineRemainder"),
        )
    }
}

@Preview
@Composable
private fun PreviewExplanationLongDivision() {
    NumberSystemsTheme {
        Surface {
            ExplanationLongDivision(
                LongDivision(
                    dividend = 256.toBigDecimal(),
                    divisor = 2.toBigDecimal(),
                    quotient = 128.toBigDecimal(),
                    division = 256.toBigDecimal(),
                    remainder = 0.toBigDecimal(),
                )
            )
        }
    }
}


@Preview
@Composable
private fun PreviewExplanationLongDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationLongDivision(
                LongDivision(
                    dividend = 256.toBigDecimal(),
                    divisor = 2.toBigDecimal(),
                    quotient = 128.toBigDecimal(),
                    division = 256.toBigDecimal(),
                    remainder = 0.toBigDecimal(),
                )
            )
        }
    }
}
