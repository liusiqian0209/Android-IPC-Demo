// IStudentManager.aidl
package cn.liusiqian.remote.aidl;

// Declare any non-default types here with import statements
import cn.liusiqian.remote.aidl.Student;
import cn.liusiqian.remote.aidl.IOnNewStudentAddedListener;

interface IStudentManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addNewStudent(in Student student);
    List<Student> getStudents();
    void addOnNewStudentAddedListener(in IOnNewStudentAddedListener listener);
    void removeOnNewStudentAddedListener(in IOnNewStudentAddedListener listener);
}
