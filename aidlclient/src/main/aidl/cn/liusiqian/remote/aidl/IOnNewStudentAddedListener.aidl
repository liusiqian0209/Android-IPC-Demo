// IOnNewStudentAddedListener.aidl
package cn.liusiqian.remote.aidl;

import cn.liusiqian.remote.aidl.Student;

interface IOnNewStudentAddedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewStudentAdded(in Student student);

}
