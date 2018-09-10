package org.bh.app.talkRadio.struct



import org.bh.app.talkRadio.struct.ComputingDifficulty.*
import org.bh.tools.base.abstraction.*
import org.bh.tools.base.collections.extensions.*
import org.bh.tools.base.math.*
import org.bh.tools.base.math.geometry.*
import org.bh.tools.base.util.*
import org.bh.tools.io.databases.exposed.ExposedDB
import java.util.*


/**
 * @author Ben
 * @since 2018-07-22
 */
open class AutoPlaylist(var rules: List<MediaItemSelectionRule>): Playlist {

    private var cachedItems: List<MediaItem>? = null

    override val items: List<MediaItem> get() = getOrGenerateItems()


    private fun getOrGenerateItems(): List<MediaItem> {
        cachedItems?.let { return it }

        val generatedItems = generateItems()
        this.cachedItems = generatedItems
        return generatedItems
    }


    private fun generateItems(): List<MediaItem> {
        ExposedDB()
        TODO("not implemented")
    }


    fun contains(item: MediaItem): Boolean {
        return rules.any { it.appliesTo(item) }
    }
}



interface MediaItemSelectionRule {
    /**
     * How difficult is it to compute this selection rule?
     * This will be used to decide how often to call [appliesTo] when refreshing an [AutoPlaylist]
     */
    val computingDifficulty: ComputingDifficulty

    /**
     * Determines whether this rule applies to the given media item
     */
    fun appliesTo(item: MediaItem): Boolean
}



/**
 * About how difficult is it to compute something?
 */
enum class ComputingDifficulty {
    /** The computation in question can be done rapidly without consequence */
    quickAndEasy,

    /** The computation should not be done lightly; it may have to be done on a background thread */
    considerable,

    /** The computation is very complex and cannot be done often; it should always be performed on a background thread */
    veryComplex,

    ;

    companion object {
        inline val justComparingStrings: ComputingDifficulty get() = quickAndEasy
    }
}



class RecentPodcastEpisodeRule(val maxPlayCount: Integer = 2,
                               val maxTimeSinceFirstPlay: TimeInterval,
                               val maxTimeSinceLastPlay: TimeInterval,
                               val maxTimeSinceRelease: TimeInterval,
                               val rejectOldEpisodes: Boolean = true): MediaItemSelectionRule {
    override val computingDifficulty = considerable

    override fun appliesTo(item: MediaItem): Boolean {
        when (item.genre) {
            MediaItem.Genre.genericMusic -> return false
            MediaItem.Genre.podcast -> {
                if (item.wasNeverPlayed) {
                    return false
                }

                if (item.playCount > maxPlayCount) {
                    return false
                }

                val secondsSinceLastPlay = item.lastPlayDate?.timeIntervalSinceNow?.invertedSign() ?: -Fraction.infinity

                if (secondsSinceLastPlay > maxTimeSinceLastPlay) {
                    return false
                }

                val secondsSinceFirstPlay = item.firstPlayDate?.timeIntervalSinceNow?.invertedSign() ?: -Fraction.infinity

                if (secondsSinceFirstPlay > maxTimeSinceFirstPlay) {
                    return false
                }

                val secondsSinceRelease = item.releaseDate?.timeIntervalSinceNow?.invertedSign() ?: -Fraction.infinity

                if (secondsSinceRelease > maxTimeSinceRelease) {
                    return false
                }

                if (rejectOldEpisodes) {
                    val interveningEpisodeCount = item.entirePodcast?.episodeCount(since= item) ?: 0
                    if (interveningEpisodeCount > 0) {
                        return false
                    }
                }

                return true
            }
            is MediaItem.Genre.other -> return false
        }
    }
}



private val MediaItem.entirePodcast: PodcastShowAutoPlaylist?
    get() {
        TODO("not implemented")
    }

private inline val MediaItem.wasEverPlayed: Boolean
    get() = playCount > 0

private inline val MediaItem.wasNeverPlayed: Boolean
    get() = !wasEverPlayed

private val MediaItem.firstPlayDate: Date?
    get() {
        TODO("not implemented")
    }
private val MediaItem.lastPlayDate: Date?
    get() {
        TODO("not implemented")
    }
private val MediaItem.releaseDate: Date?
    get() {
        TODO("not implemented")
    }
private val MediaItem.addedDate: Date?
    get() {
        TODO("not implemented")
    }
private val MediaItem.podcastShowName: String?
    get() {
        TODO("Not implemented")
    }



class PodcastShowAutoPlaylist(val podcastShowName: String) : AutoPlaylist(listOf(PodcastShowSelectionRule(podcastShowName))) {

    val allEpisodes get() = items

    /**
     * Finds the number of episodes of this show since the given one, such that is that one is the latest, this returns
     * `0`. If the given episode is not in this show, `null` is returned.
     */
    fun episodeCount(since: MediaItem): Int? {
        @Suppress("UnnecessaryVariable")
        val possibleEpisode = since
        val allEpisodesSorted = this.allEpisodes.sortedByReleaseDate()

        // if it's already sorted, assuming we're up-to-date, the index is the same as the number of episodes since the
        // latest, which would be `0` if this is the latest.
        return allEpisodesSorted.indexOrNull(of= possibleEpisode)
    }
}



class PodcastShowSelectionRule(val podcastShowName: String): MediaItemSelectionRule {
    override val computingDifficulty: ComputingDifficulty
        get() = ComputingDifficulty.justComparingStrings

    override fun appliesTo(item: MediaItem): Boolean {
        item.podcastShowName?.let { itemPodcastShowName ->
            return itemPodcastShowName == podcastShowName
        }
        return false
    }
}



fun Iterable<MediaItem>.sortedByReleaseDate() = this.sorted { lhs, rhs ->
    val lhsReleaseDate = lhs.releaseDate ?: return@sorted ComparisonResult.indeterminate
    val rhsReleaseDate = rhs.releaseDate ?: return@sorted ComparisonResult.indeterminate
    return@sorted lhsReleaseDate.compare(rhsReleaseDate)
}
