package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "labeledlatlng")
data class LabeledLatLng(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @SerializedName("label")
    @Expose
    var label: String?,

    @SerializedName("lat")
    @Expose
    var lat: Double?,

    @SerializedName("lng")
    @Expose
    val lng: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(label)
        parcel.writeValue(lat)
        parcel.writeValue(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabeledLatLng> {
        override fun createFromParcel(parcel: Parcel): LabeledLatLng {
            return LabeledLatLng(parcel)
        }

        override fun newArray(size: Int): Array<LabeledLatLng?> {
            return arrayOfNulls(size)
        }
    }
}