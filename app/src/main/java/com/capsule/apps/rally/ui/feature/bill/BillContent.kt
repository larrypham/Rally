package com.capsule.apps.rally.ui.feature.bill

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.capsule.apps.rally.R
import com.capsule.apps.rally.data.Bill
import com.capsule.apps.rally.ui.common.BillRow
import com.capsule.apps.rally.ui.feature.detail.StatementBody

@Composable
fun BillsContent(bills: List<Bill>) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills,
        colors = { bill -> bill.color },
        amounts = { bill -> bill.amount },
        amountsTotal = bills.map { bill -> bill.amount }.sum(),
        circleLabel = stringResource(id = R.string.due)
    ) { bill ->
        BillRow(name = bill.name, due = bill.due, amount = bill.amount, color = bill.color)
    }
}