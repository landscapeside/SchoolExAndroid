package com.edge.reader;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;

import java.lang.reflect.Method;

/**
 * Created by 1 on 2016/3/7.
 */
public class NetBuilder {

    private NetEdge buildInstance = null;

    private NetEdge.INetListener mNetListener = null;
    public NetBuilder register(NetEdge.INetListener netListener){
        mNetListener = netListener;
        return this;
    }

    public NetEdge build(){
        buildInstance = new NetEdge();
        buildInstance.setNetListener(mNetListener);
        return buildInstance;
    }

    public static class NetEdge {

        private NetEdge(){}

        public void call(Method method){
            if (method.isAnnotationPresent(NetBegin.class)){
                if (mNetListener != null){
                    mNetListener.netBegin();
                }
            }else if (method.isAnnotationPresent(NetEnd.class)){
                if (mNetListener != null){
                    mNetListener.netEnd();
                }
            }
        }

        private INetListener mNetListener = null;

        void setNetListener(INetListener netListener) {
            mNetListener = netListener;
        }
        public interface INetListener{
            void netBegin();
            void netEnd();
        }

    }

}
