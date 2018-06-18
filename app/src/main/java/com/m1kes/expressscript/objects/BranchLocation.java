package com.m1kes.expressscript.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class BranchLocation implements Parcelable {

	protected String name, address, city, retailTypeName;
    protected double latitude, longitude;

	public String getCity() {
		return city;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getType() {
		return retailTypeName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Constructor
	 */
	public BranchLocation() {
	}

	public BranchLocation(Parcel in) {
		latitude = in.readDouble();
		longitude = in.readDouble();
		name = in.readString();
		address = in.readString();
		city = in.readString();
		retailTypeName = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(city);
		dest.writeString(retailTypeName);
	}

	/**
	 * Interface that must be implemented and provided as a public CREATOR field that
	 * generates instances of the Parcelable class from a Parcel.
	 */
	public static final Creator<BranchLocation> CREATOR = new Creator<BranchLocation>() {
		public BranchLocation createFromParcel(Parcel in) {
			return new BranchLocation(in);
		}

		public BranchLocation[] newArray(int size) {
			return new BranchLocation[size];
		}
	};
}
