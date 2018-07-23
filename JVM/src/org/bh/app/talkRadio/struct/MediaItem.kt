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
        val otherData: Map<String, Any>
)
{




    sealed class Genre {
        object music: Genre()
        object podcast: Genre()
        class other(val stringValue: String): Genre()
    }



    sealed class Source {
        class file(val file: File)
        class url(val url: URL)
        class stream(val stream: InputStream)
    }
}