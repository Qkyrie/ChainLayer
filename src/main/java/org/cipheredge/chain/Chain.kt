package org.cipheredge.chain

import org.slf4j.LoggerFactory
import org.web3j.crypto.WalletUtils
import java.util.*

enum class Chain(
    val slug: String,
    val chainId: Int,
    val secondsBetweenBlocks: Double,
    val multicallAddress: String,
    val alchemyAPISupported: Boolean = true,
    val isEVM: Boolean = true,
    val addressValidator: AddressValidator = EvmAddressValidator(),
    val alternativeNames: List<String> = Collections.emptyList()
) {

    BASE(
        slug = "base",
        chainId = 8453,
        secondsBetweenBlocks = 2.0,
        multicallAddress = "0x8Fe37d605Aa5f2fE4220E713d69a82443aF59C28"
    );

    companion object {

        val logger = LoggerFactory.getLogger(this::class.java)


        fun isSupported(str: String): Boolean {
            return fromStringOrNull(str) != null
        }

        @OptIn(ExperimentalStdlibApi::class)
        fun fromChainId(chainId: Int): Chain? {
            return entries.firstOrNull {
                it.chainId == chainId
            }
        }

        fun fromString(str: String): Chain {
            return fromStringOrNull(str) ?: throw IllegalArgumentException("Unsupported chain: $str")
        }

        @OptIn(ExperimentalStdlibApi::class)
        fun fromStringOrNull(str: String?): Chain? {

            if (str == null) return null

            val cleaned = str.replace(" ", "-")

            return entries.firstOrNull {
                it.name.lowercase() == cleaned.lowercase() || it.slug.lowercase() == cleaned.lowercase() || it.alternativeNames.any { alternative ->
                    alternative.lowercase() == cleaned.lowercase()
                }
            } ?: run {
                logger.debug("Unsupported chain: $str")
                null
            }
        }
    }

    interface AddressValidator {
        fun isValid(address: String): Boolean
    }

    class EvmAddressValidator : AddressValidator {
        override fun isValid(address: String): Boolean {
            return WalletUtils.isValidAddress(address)
        }
    }
}