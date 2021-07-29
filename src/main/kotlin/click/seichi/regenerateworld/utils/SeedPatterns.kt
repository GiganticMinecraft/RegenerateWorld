package click.seichi.regenerateworld.utils

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

    /**
     * Seed値の設定が必要かどうか。
     */
    fun isSeedNecessary() = this == NEW_SEED
}