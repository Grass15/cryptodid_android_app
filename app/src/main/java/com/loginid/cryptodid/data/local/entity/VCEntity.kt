package com.loginid.cryptodid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.utils.Constants.VC_TABLE_NAME
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@Entity(tableName = VC_TABLE_NAME)
data class VCEntity(
    @PrimaryKey
    val id: String,
    val vc: Claim? = null,
    val claimOwner: String,
    val vcType: VCType? = VCType.DEFAULT,
    val vcTitle: String? = "RANDOM_VC",
    val version: Int = 0
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