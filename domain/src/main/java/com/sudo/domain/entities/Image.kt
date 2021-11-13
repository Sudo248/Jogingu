package com.sudo.domain.entities

data class Image(
    val byteArray: ByteArray,
    val url: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (!byteArray.contentEquals(other.byteArray)) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = byteArray.contentHashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}