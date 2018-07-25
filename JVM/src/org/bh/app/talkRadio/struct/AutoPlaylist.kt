package org.bh.app.talkRadio.struct



import org.bh.tools.io.databases.exposed.ExposedDB



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
    veryComplex
}
