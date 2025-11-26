package com.yasumu.core.domain.model

@JvmInline
value class LocationOrder(val value: Int)

/**
 * 保管場所（冷凍室・冷蔵室・野菜室・サブ冷凍庫など）
 */
data class Location(
    val id: LocationId,
    val name: String,
    val order: LocationOrder,
)

