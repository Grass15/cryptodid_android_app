package com.learning.walletv21.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.learning.walletv21.core.protocols.MG_FHE
import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.utils.Constants.VC_TABLE_NAME
import kotlinx.serialization.Serializable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime

@Entity(tableName = VC_TABLE_NAME)
data class VCEntity(
    @PrimaryKey
    val id: String,
    val vc: Claim? = null,
    val claimOwner: String
)

class Claim_TypeConverter {

    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): Claim {
        val bis = ByteArrayInputStream(byteArray)
        val ois = ObjectInputStream(bis)
        return ois.readObject() as Claim
    }

    @TypeConverter
    fun toByteArray(claim: Claim): ByteArray {
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(claim)
        return bos.toByteArray()
    }
}