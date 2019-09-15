package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"], tableName = "venue")
data class Venue(

    @SerializedName("beenHere")
    @Expose
    var beenHere: BeenHere?,

    @SerializedName("categories")
    @Expose
    var categories: List<Category>?,

    @SerializedName("hereNow")
    @Expose
    var hereNow: HereNow?,

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("location")
    @Expose
    var location: Location?,

    @SerializedName("name")
    @Expose
    var name: String?,

    @SerializedName("photos")
    @Expose
    var photos: Photos?,

    @SerializedName("stats")
    @Expose
    var stats: Stats?,

    @SerializedName("verified")
    @Expose
    var verified: Boolean?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(BeenHere::class.java.classLoader),
        parcel.createTypedArrayList(Category),
        parcel.readParcelable(HereNow::class.java.classLoader),
        parcel.readString()!!,
        parcel.readParcelable(Location::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Photos::class.java.classLoader) as? Photos,
        parcel.readParcelable(Stats::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(beenHere, flags)
        parcel.writeTypedList(categories)
        parcel.writeParcelable(hereNow, flags)
        parcel.writeString(id)
        parcel.writeParcelable(location, flags)
        parcel.writeString(name)
        parcel.writeParcelable(photos, flags)
        parcel.writeParcelable(stats, flags)
        parcel.writeValue(verified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Venue> {
        override fun createFromParcel(parcel: Parcel): Venue {
            return Venue(parcel)
        }

        override fun newArray(size: Int): Array<Venue?> {
            return arrayOfNulls(size)
        }
    }
}