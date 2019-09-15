package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "beenhere")
data class BeenHere(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @SerializedName("count")
    @Expose
    var count: Int?,

    @SerializedName("lastCheckinExpiredAt")
    @Expose
    var lastCheckinExpiredAt: Int?,

    @SerializedName("marked")
    @Expose
    val marked: Boolean,

    @SerializedName("unconfirmedCount")
    @Expose
    val unconfirmedCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(count)
        parcel.writeValue(lastCheckinExpiredAt)
        parcel.writeByte(if (marked) 1 else 0)
        parcel.writeInt(unconfirmedCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BeenHere> {
        override fun createFromParcel(parcel: Parcel): BeenHere {
            return BeenHere(parcel)
        }

        override fun newArray(size: Int): Array<BeenHere?> {
            return arrayOfNulls(size)
        }
    }
}