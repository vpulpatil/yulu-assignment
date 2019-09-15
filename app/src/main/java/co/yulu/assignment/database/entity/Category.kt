package co.yulu.assignment.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"], tableName = "category")
data class Category(

    @SerializedName("icon")
    @Expose
    var icon: Icon?,

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String?,

    @SerializedName("pluralName")
    @Expose
    var pluralName: String?,

    @SerializedName("primary")
    @Expose
    var primary: Boolean = false,

    @SerializedName("shortName")
    @Expose
    var shortName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Icon::class.java.classLoader),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(icon, flags)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(pluralName)
        parcel.writeByte(if (primary) 1 else 0)
        parcel.writeString(shortName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}