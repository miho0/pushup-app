package com.example.pushupapp

import android.os.Parcel
import android.os.Parcelable

class PushupEntry(val player: Int, val amount: Int, val time: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(player)
        parcel.writeInt(amount)
        parcel.writeDouble(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PushupEntry> {
        override fun createFromParcel(parcel: Parcel): PushupEntry {
            return PushupEntry(parcel)
        }

        override fun newArray(size: Int): Array<PushupEntry?> {
            return arrayOfNulls(size)
        }
    }
}
