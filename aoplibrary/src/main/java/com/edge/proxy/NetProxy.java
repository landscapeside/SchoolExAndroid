package com.edge.proxy;

import com.edge.reader.NetBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by admin on 2016/2/2.
 */
public class NetProxy implements InvocationHandler {

    private Object tarjectObject;
    private NetBuilder.NetEdge netEdge;

    public NetProxy(NetBuilder.NetEdge netEdge){
        if (netEdge == null)
            throw new IllegalArgumentException("netedge is null");
        this.netEdge = netEdge;
    }

    public Object createProxyInstance(Object object){
        tarjectObject = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        netEdge.call(method);
        return method.invoke(tarjectObject,args);
    }
}
