package com.example.myapplication.Entity;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class MySearchSuggest implements  SearchSuggestion {
    private String label;

    public MySearchSuggest(String suggestion) {
        this.label = suggestion.toLowerCase();
    }

    public MySearchSuggest(Parcel source) {
        this.label = source.readString();
    }


    @Override
    public String getBody() {
        return label;
    }

    public static final Creator<MySearchSuggest> CREATOR = new Creator<MySearchSuggest>() {
        @Override
        public MySearchSuggest createFromParcel(Parcel in) {
            return new MySearchSuggest(in);
        }

        @Override
        public MySearchSuggest[] newArray(int size) {
            return new MySearchSuggest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
    }
}
