package org.bh.app.talkRadio.struct

import org.bh.tools.base.abstraction.*
import java.io.*
import java.net.*
import java.util.*

/**
 * @author Ben Leggiero
 * @since 2018-06-26
 */
class MediaItem
(
        val id: UUID,
        val title: String,
        val genre: Genre,
        val source: Source,

        val rating: Fraction,
        val playCount: Integer,
        val otherData: Map<String, Serializable>
)
{


    sealed class Genre(val serialName: String) {
        object genericMusic: Genre(serialName = "genericMusic")
        object podcast: Genre(serialName = "podcast")
        class other(val stringValue: String): Genre(serialName = stringValue)
    }



    sealed class Source {
        class file(val file: File)
        class url(val url: URL)
    }
}