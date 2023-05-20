package com.capsule.apps.rally.ui.feature.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capsule.apps.rally.R
import com.capsule.apps.rally.data.Bill
import com.capsule.apps.rally.data.UserData
import com.capsule.apps.rally.ui.common.AccountRow
import com.capsule.apps.rally.ui.common.BillRow
import com.capsule.apps.rally.ui.common.RallyAlertDialog
import com.capsule.apps.rally.ui.common.RallyDivider
import com.capsule.apps.rally.ui.common.formatAmount
import com.capsule.apps.rally.ui.theme.RallyTheme
import java.util.Locale

@Composable
fun OverviewContent(
    onClickSeeAllAccounts: () -> Unit = {},
    onClickSeeAllBills: () -> Unit = {},
    onAccountClick: (String) -> Unit = {}
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
        .semantics { contentDescription = "Overview Screen" }) {
        AlertCard()
        Spacer(Modifier.height(RallyDefaultPadding))
        AccountsCard(onClickSeeAllAccounts, onAccountClick)
        Spacer(Modifier.height(RallyDefaultPadding))
        BillsCard(onClickSeeAllBills)
    }
}


@Composable
private fun AlertCard() {
    var showDialog by remember { mutableStateOf(false) }
    val alertMessage = "Heads up, you've used up 90% of your shopping budget for this month..."

    if (showDialog) {
        RallyAlertDialog(onDismiss = {
            showDialog = false
        }, bodyText = alertMessage, buttonText = "Dismiss".uppercase(Locale.getDefault()))
    }

    Card {
        Column {
            AlertHeader {
                showDialog = true
            }
            RallyDivider(
                modifier = Modifier.padding(start = RallyDefaultPadding, end = RallyDefaultPadding)
            )
            AlertItem(message = alertMessage)
        }
    }
}

@Composable
private fun AlertHeader(onClickSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(RallyDefaultPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Alerts",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        TextButton(
            onClick = onClickSeeAll,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "SEE ALL", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
private fun AlertItem(message: String) {
    Row(
        modifier = Modifier
            .padding(RallyDefaultPadding)
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(style = MaterialTheme.typography.body2, modifier = Modifier.weight(1f), text = message)
        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.Top)
                .clearAndSetSemantics { }) {
            Icon(Icons.Filled.Sort, contentDescription = null)
        }
    }
}

@Composable
private fun <T> OverviewScreenCard(
    title: String,
    amount: Float,
    onClickSeeAll: () -> Unit,
    values: (T) -> Float,
    colors: (T) -> Color,
    data: List<T>,
    row: @Composable (T) -> Unit
) {
    Card {
        Column {
            Column(Modifier.padding(RallyDefaultPadding)) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
                val amountText = "$" + formatAmount(amount)
                Text(text = amountText, style = MaterialTheme.typography.h2)
            }
            OverviewDivider(data = data, values = values, colors = colors)
            Column(Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)) {
                data.take(SHOW_ITEMS).forEach { row(it) }
                SeeAllButton(modifier = Modifier.clearAndSetSemantics {
                    contentDescription = "All $title"
                }, onClick = onClickSeeAll)
            }
        }
    }
}

@Composable
private fun <T> OverviewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: (T) -> Color
) {
    Row(Modifier.fillMaxWidth()) {
        data.forEach { item: T ->
            Spacer(
                modifier = Modifier
                    .weight(values(item))
                    .height(1.dp)
                    .background(colors(item))
            )
        }
    }
}

@Composable
private fun AccountsCard(onClickSeeAll: () -> Unit, onAccountClick: (String) -> Unit) {
    val amount = UserData.accounts.map { account -> account.balance }.sum()
    OverviewScreenCard(
        title = stringResource(id = R.string.accounts),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        values = { it.balance },
        colors = { it.color },
        data = UserData.accounts
    ) { account ->
        AccountRow(
            modifier = Modifier.clickable { onAccountClick(account.name) },
            name = account.name,
            number = account.number,
            amount = account.balance,
            color = account.color
        )
    }
}

@Composable
private fun BillsCard(onClickSeeAll: () -> Unit) {
    val amount = UserData.bills.map { bill: Bill -> bill.amount }.sum()
    OverviewScreenCard(
        title = stringResource(id = R.string.bills),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        values = { it.amount },
        colors = { it.color },
        data = UserData.bills
    ) { bill ->
        BillRow(name = bill.name, due = bill.due, amount = bill.amount, color = bill.color)
    }
}

@Composable
private fun SeeAllButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick, modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.see_all))
    }
}


private val RallyDefaultPadding = 12.dp

private const val SHOW_ITEMS = 3

@Preview(showBackground = true)
@Composable
fun OverviewPreview() {
    RallyTheme {
        OverviewContent()
    }
}