package click.seichi.regenerateworld.utils

import java.time.ZonedDateTime
import java.util.*

/**
 * 再生成の計画及び必要な情報を集めたデータクラス。
 */
data class Plan(
    /**
     * UniqueID。
     */
    val id: UUID,

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
     * Seed値に関する設定。詳細は[[SeedPatterns]]を参照。
     */
    val seedPatterns: SeedPatterns,

    /**
     * Seed値を設定する。詳細は[[SeedPatterns]]を参照。
     */
    val seed: String?,

    /**
     * 再生成を行うワールド群。
     */
    val worlds: List<String>
)
