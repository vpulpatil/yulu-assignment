package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "herenow")
data class HereNow(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @SerializedName("count")
    @Expose
    var count: Int?,

    @SerializedName("groups")
    @Expose
    var groups: List<Group>?,

    @SerializedName("summary")
    @Expose
    var summary: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(Group),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(count)
        parcel.writeTypedList(groups)
        parcel.writeString(summary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HereNow> {
        override fun createFromParcel(parcel: Parcel): HereNow {
            return HereNow(parcel)
        }

        override fun newArray(size: Int): Array<HereNow?> {
            return arrayOfNulls(size)
        }
    }
}
