package cn.liusiqian.remote.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Créé par liusiqian 16/10/31.
 */

public class Student implements Parcelable
{
    private String id;
    private String name;
    private boolean isMale;

    public Student(String id, String name, boolean isMale)
    {
        this.id = id;
        this.name = name;
        this.isMale = isMale;
    }

    protected Student(Parcel in)
    {
        id = in.readString();
        name = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>()
    {
        @Override
        public Student createFromParcel(Parcel in)
        {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size)
        {
            return new Student[size];
        }
    };

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Student name is ").append(name).append(", ID:").append(id).append(", gender:")
                .append(isMale?"Male":"Female");
        return sb.toString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isMale ? 1 : 0));
    }
}
