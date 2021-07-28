package click.seichi.regenerateworld.utils

enum class SeedType(val isNewSeed: Boolean, val isRandomSeed: Boolean) {
    CURRENT_SEED(false, false),
    NEW_SEED(true, false),
    RANDOM_NEW_SEED(true, true);

    fun isSeedNecessary() = this == NEW_SEED
}