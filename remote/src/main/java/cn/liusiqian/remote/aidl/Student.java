package cn.liusiqian.remote.aidl;

/**
 * Créé par liusiqian 16/10/31.
 */

public class Student
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Student name is ").append(name).append(", ID:").append(id).append(", gender:")
                .append(isMale?"Male":"Female");
        return sb.toString();
    }
}
