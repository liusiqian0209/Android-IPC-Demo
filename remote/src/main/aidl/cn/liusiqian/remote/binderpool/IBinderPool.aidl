// IBinderPool.aidl
package cn.liusiqian.remote.binderpool;

interface IBinderPool {
    IBinder queryBinder(in int code);
}
