package com.example.destroyer.lab1;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable{
    private String title;
    private int imageId;
    private String description;

    public ListItem(){
    }

    public ListItem(String title, int imageId, String description){
        this.title = title;
        this.imageId = imageId;
        this.description = description;
    }
    //getters and setters
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getImageId(){
        return imageId;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    //parcelable implementation for passing data between activities
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeInt(imageId);
        parcel.writeString(description);
    }

    public static final Parcelable.Creator<ListItem> CREATOR = new Creator<ListItem>() {

        public ListItem createFromParcel(Parcel source) {
            ListItem item = new ListItem();
            item.title = source.readString();
            item.imageId = source.readInt();
            item.description = source.readString();
            return item;
        }
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

}
