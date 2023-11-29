// Copyright 2023 Nacho Lopez
// SPDX-License-Identifier: Apache-2.0
package io.nlopez.compose.rules

import io.nlopez.rules.core.ComposeKtConfig
import io.nlopez.rules.core.ComposeKtVisitor
import io.nlopez.rules.core.Emitter
import io.nlopez.rules.core.report
import io.nlopez.rules.core.util.isTypeMutable
import org.jetbrains.kotlin.psi.KtFunction

class MutableParameters : ComposeKtVisitor {

    override fun visitComposable(
        function: KtFunction,
        autoCorrect: Boolean,
        emitter: Emitter,
        config: ComposeKtConfig,
    ) {
        function.valueParameters
            .filter { it.isTypeMutable }
            .forEach { emitter.report(it, MutableParameterInCompose) }
    }

    companion object {

        val MutableParameterInCompose = """
            Using mutable objects as state in Compose will cause your users to see incorrect or stale data in your app.
            Mutable objects that are not observable, such as ArrayList<T> or a mutable data class, cannot be observed by
            Compose to trigger recomposition when they change.

            See https://mrmans0n.github.io/compose-rules/rules/#do-not-use-inherently-mutable-types-as-parameters for more information.
        """.trimIndent()
    }
}
