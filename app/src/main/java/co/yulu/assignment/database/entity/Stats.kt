package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stats")
data class Stats(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @SerializedName("checkinsCount")
    @Expose
    var checkinsCount: Int?,

    @SerializedName("tipCount")
    @Expose
    var tipCount: Int?,

    @SerializedName("usersCount")
    @Expose
    var usersCount: Int?,

    @SerializedName("visitsCount")
    @Expose
    var visitsCount: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(checkinsCount)
        parcel.writeValue(tipCount)
        parcel.writeValue(usersCount)
        parcel.writeValue(visitsCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stats> {
        override fun createFromParcel(parcel: Parcel): Stats {
            return Stats(parcel)
        }

        override fun newArray(size: Int): Array<Stats?> {
            return arrayOfNulls(size)
        }
    }
}