package com.capsule.apps.rally.ui.feature.account

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.capsule.apps.rally.R
import com.capsule.apps.rally.data.Account
import com.capsule.apps.rally.ui.common.AccountRow
import com.capsule.apps.rally.ui.feature.detail.StatementBody


@Composable
fun AccountsContent(
    accounts: List<Account>,
    onAccountClick: (String) -> Unit = {}
) {
    StatementBody(
        modifier = Modifier.semantics { contentDescription = "Accounts Screen" },
        items = accounts,
        colors = { account -> account.color },
        amounts = { account -> account.balance },
        amountsTotal = accounts.map { account -> account.balance }.sum(),
        circleLabel = stringResource(id = R.string.total)
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
fun SingleAccount(account: Account) {
    StatementBody(
        items = listOf(account),
        colors = { account.color },
        amounts = { account.balance },
        amountsTotal = account.balance,
        circleLabel = account.name
    ) { row ->
        AccountRow(
            name = row.name,
            number = row.number,
            amount = row.balance,
            color = row.color
        )
    }
}