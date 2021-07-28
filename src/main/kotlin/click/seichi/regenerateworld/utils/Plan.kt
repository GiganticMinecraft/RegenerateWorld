package click.seichi.regenerateworld.utils

import java.time.ZonedDateTime

/**
 * 再生成の計画及び必要な情報を集めたデータクラス。
 */
data class Plan(
    /**
     * UniqueID。TODO: 自動で割り振るUUID
     */
    val id: String,

    /**
     * BukkitScheduler上のタスクID。
     */
    val bukkitTaskId: Int,

    /**
     * 最後に再生成を実行した日時。
     */
    val lastRegeneratedDate: ZonedDateTime,

    /**
     * 再生成を行う間隔。
     */
    val interval: Long,

    /**
     * Seed値に関する設定。詳細は[[SeedType]]を参照。
     */
    val seedType: SeedType,

    /**
     * Seed値を設定する。詳細は[[SeedType]]を参照。
     */
    val seed: String?,

    /**
     * 再生成を行うワールド群。
     */
    val worlds: List<String>
)
