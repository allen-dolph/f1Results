package com.allendolph.f1results.api;

import android.content.ContentValues;

import com.allendolph.f1results.data.F1Contract;
import com.google.gson.annotations.SerializedName;

/**
 * Created by allendolph on 4/8/15.
 */
public class F1ResultsModel {

    public class F1ResultResponse {
        @SerializedName("MRData")
        public MRData mrData;
    }

    public class MRData {
        public String xmlns;
        public String series;
        public int limit;
        public int offset;
        public int total;
        @SerializedName("RaceTable")
        public RaceTable raceTable;
    }

    public class RaceTable {
        public String season;
        public String round;
        @SerializedName("Races")
        public Race[] races;
    }

    public class Race {
        public String season;
        public int round;
        public String url;
        public String raceName;
        public String date;
        public String time;
        @SerializedName("Circuit")
        public Circuit circuit;
        @SerializedName("Results")
        public RaceResult[] results;

        public ContentValues getAsRaceEntryContentValues(long circuitId) {
            ContentValues values = new ContentValues();

            values.put(F1Contract.RaceEntry.COLUMN_CIRCUIT_ID, circuitId);
            values.put(F1Contract.RaceEntry.COLUMN_SEASON, this.season);
            values.put(F1Contract.RaceEntry.COLUMN_ROUND, this.round);
            values.put(F1Contract.RaceEntry.COLUMN_URL, this.url);
            values.put(F1Contract.RaceEntry.COLUMN_RACE_NAME, this.raceName);
            values.put(F1Contract.RaceEntry.COLUMN_DATE, this.date);
            values.put(F1Contract.RaceEntry.COLUMN_TIME, this.time);

            return values;
        }
    }

    public class Circuit {
        public String circuitId;
        public String url;
        public String raceName;
        @SerializedName("Location")
        public Location location;

        public ContentValues getAsCircuitEntryContentValues() {
            ContentValues values = new ContentValues();

            values.put(F1Contract.CircuitEntry.COLUMN_CIRCUIT_ID, this.circuitId);
            values.put(F1Contract.CircuitEntry.COLUMN_URL, this.url);
            values.put(F1Contract.CircuitEntry.COLUMN_CIRCUIT_NAME, this.url);
            values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LAT, this.location.lat);
            values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LONG, this.location.lng);
            values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_LOCALITY, this.location.locality);
            values.put(F1Contract.CircuitEntry.COLUMN_LOCATION_COUNTRY, this.location.country);

            return values;
        }
    }

    public class Location {
        public double lat;
        public double lng;
        public String locality;
        public String country;
    }

    public class Driver {
        public String driverId;
        public int permanentNumber;
        public String code;
        public String url;
        public String givenName;
        public String familyName;
        public String dateOfBirth;
        public String nationality;
    }

    public class Constructor {
        public String constructorId;
        public String url;
        public String name;
        public String nationality;
    }

    public class Time {
        public long millis;
        public String time;
    }

    public class AverageSpeed {
        public String units;
        public double speed;
    }

    public class FastestLap {
        public int rank;
        public int lap;
        @SerializedName("Time")
        public Time time;
        @SerializedName("FastestLap")
        public FastestLap fastestLap;
    }

    public class RaceResult {
        public int number;
        public int position;
        public String positionText;
        public int points;
        @SerializedName("Driver")
        public Driver driver;
        @SerializedName("Constructor")
        public Constructor constructor;
        public int grid;
        public int laps;
        public String status;
        @SerializedName("Time")
        public Time time;
        @SerializedName("FastestLap")
        public FastestLap fastestLap;
    }
}
