package com.github.pedramrn.slick.parent.datasource.network.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class IpLocation {

    @Nullable
    @SerializedName("status")
    public abstract String status();

    @Nullable
    @SerializedName("country")
    public abstract String country();

    @Nullable
    @SerializedName("countryCode")
    public abstract String countryCode();

    public static IpLocation create(String status, String country, String countryCode) {
        return new AutoValue_IpLocation(
                status,
                country,
                countryCode
        );
    }

	/*@Nullable
    @SerializedName("city")
	public abstract String city();

	@Nullable
	@SerializedName("zip")
	public abstract String zip();

	@Nullable
	@SerializedName("org")
	public abstract String org();

	@Nullable
	@SerializedName("timezone")
	public abstract String timezone();

	@Nullable
	@SerializedName("regionName")
	public abstract String regionName();

	@Nullable
	@SerializedName("isp")
	public abstract String isp();

	@Nullable
	@SerializedName("query")
	public abstract String query();

	@Nullable
	@SerializedName("lon")
	public abstract String lon();

	@Nullable
	@SerializedName("lat")
	public abstract String lat();

	@Nullable
	@SerializedName("as")
	public abstract String as();

	@Nullable
	@SerializedName("region")
	public abstract String region();*/

    public static TypeAdapter<IpLocation> typeAdapter(Gson gson) {
        return new AutoValue_IpLocation.GsonTypeAdapter(gson);
    }
}