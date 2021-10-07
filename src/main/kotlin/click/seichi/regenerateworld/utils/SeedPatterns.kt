package click.seichi.regenerateworld.utils

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.toResultOr

/**
 * Seed値のパターンをまとめた列挙クラス。
 * @param isNewSeed 新しいSeed値を使うかどうか。
 * @param isRandomSeed Seed値をランダムに決めるかどうか。
 */
enum class SeedPatterns(private val isNewSeed: Boolean, private val isRandomSeed: Boolean) {
    /**
     * 現在のSeed値をそのまま利用する。
     */
    CURRENT_SEED(false, false),

    /**
     * Seed値を新しく生成し、その値は自分で指定したものを用いる。
     */
    NEW_SEED(true, false),

    /**
     * Seed値を新しく生成し、その値はランダムで生成する。
     */
    RANDOM_NEW_SEED(true, true);

    companion object {
        fun safeValueOf(type: String): Result<SeedPatterns, SeedPatternsError> = runCatching {
            java.lang.Enum.valueOf(SeedPatterns::class.java, type)
        }.getOrNull().toResultOr { SeedPatternsError.SEED_PATTERN_NOT_FOUND }
    }

    /**
     * Seed値の設定が必要かどうか。
     */
    fun isSeedNecessary() = this == NEW_SEED
}

enum class SeedPatternsError(private val reason: String) : IError {
    SEED_PATTERN_NOT_FOUND("指定されたシード値パターンは存在しません。");

    override fun errorName() = this.name
    override fun reason() = this.reason
}