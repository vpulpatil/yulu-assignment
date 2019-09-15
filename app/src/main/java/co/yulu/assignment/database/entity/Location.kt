package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "location")
data class Location(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @SerializedName("address")
    @Expose
    var address: String?,

    @SerializedName("cc")
    @Expose
    var cc: String?,

    @SerializedName("city")
    @Expose
    var city: String?,

    @SerializedName("country")
    @Expose
    var country: String?,

    @SerializedName("crossStreet")
    @Expose
    var crossStreet: String?,

    @SerializedName("distance")
    @Expose
    var distance: Int?,

    @SerializedName("labeledLatLng")
    @Expose
    var labeledLatLngs: List<LabeledLatLng>?,

    @SerializedName("lat")
    @Expose
    var lat: Double,

    @SerializedName("lng")
    @Expose
    var lng: Double,

    @SerializedName("postalCode")
    @Expose
    var postalCode: String?,

    @SerializedName("state")
    @Expose
    var state: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(LabeledLatLng),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(address)
        parcel.writeString(cc)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(crossStreet)
        parcel.writeValue(distance)
        parcel.writeTypedList(labeledLatLngs)
        parcel.writeValue(lat)
        parcel.writeValue(lng)
        parcel.writeString(postalCode)
        parcel.writeString(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}