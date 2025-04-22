package org.cipheredge.chain.evm.config

import org.cipheredge.chain.Chain
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class Web3jConfig(
    @Value("\${org.cipheredge.base.endpoint.url}") private val baseEndpoint: String,
) {

    @Bean
    fun web3jHolder(): Web3jHolder {
        return Web3jHolder(
            listOf(Web3(Chain.BASE, baseEndpoint))
        )
    }
}