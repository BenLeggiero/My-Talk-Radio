package org.bh.app.talkRadio.struct

import org.bh.tools.base.abstraction.Integer
import org.bh.tools.io.databases.exposed.*
import org.jetbrains.exposed.dao.*
import java.util.*

/**
 * @author Ben Leggiero
 * @since 2018-08-05
 */

object MasterDatabaseMetaTable : IntegerIdTable() {
    val sequelId = integer("sequel_id").uniqueIndex()
    val databaseVersion = integer("databaseVersion").default(1)
}



object MasterDatabaseMeta : IntegerEntity(EntityID<Integer>(0, MasterDatabaseMetaTable)) {
    object Companion : IntegerEntityClass<MasterDatabaseMeta>(MasterDatabaseMetaTable)

    var databaseVersion by MasterDatabaseMetaTable.databaseVersion
}



object SerializedMediaItemTable : UUIDTable(name = "SerializedMediaItems") {
    val title       = text("title")
    val genre       = text("genre")
    val sourceType  = enumeration("sourceType", SourceType::class.java)
    val sourceValue = text("sourceValue")

    val rating      = decimal("rating", precision = 3, scale = 2).nullable()
// TODO: Serialize misc other data
}



enum class SourceType {
    file, url
}



class SerializedMediaItem(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SerializedMediaItem>(SerializedMediaItemTable)

    var title       by SerializedMediaItemTable.title
    var genre       by SerializedMediaItemTable.genre
    var sourceType  by SerializedMediaItemTable.sourceType
    var sourceValue by SerializedMediaItemTable.sourceValue

    var rating      by SerializedMediaItemTable.rating
}
