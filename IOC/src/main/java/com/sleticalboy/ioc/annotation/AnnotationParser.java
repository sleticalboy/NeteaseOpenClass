package com.sleticalboy.ioc.annotation;

import android.util.Log;
import android.util.SparseLongArray;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created on 19-3-20.
 *
 * @author leebin
 */
public class AnnotationParser {

    /*
    getMethod(): 获取方法，包括父类、接口中的方法
    getDeclaredMethod(): 获取本类中声明的方法，不包括父类、接口中的方法
     */

    private AnnotationParser() {
        throw new AssertionError();
    }

    public static void parseAnnotations(@NonNull Object host) {
        try {
            setContentView(host);
            getViews(host);
            onEvents(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setContentView(@NonNull Object host) throws Exception {
        final Class<?> clazz = host.getClass();
        final SetContentView setContentView = clazz.getAnnotation(SetContentView.class);
        final int layoutId;
        if (setContentView != null && (layoutId = setContentView.value()) != -1) {
            // public void setContentView() {}
            clazz.getMethod("setContentView", int.class).invoke(host, layoutId);
        }
    }

    private static void getViews(@NonNull Object host) throws Exception {
        final Class<?> clazz = host.getClass();
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            final GetView getView = field.getAnnotation(GetView.class);
            final int id;
            if (getView != null && (id = getView.value()) != -1) {
                // private TextView title;
                field.setAccessible(true);
                field.set(host, clazz.getMethod("findViewById", int.class).invoke(host, id));
            }
        }
    }

    private static void onEvents(@NonNull Object host) throws Exception {
        // 找到 activity 中声明的所有方法
        final Method[] methods = host.getClass().getDeclaredMethods();
        for (Method method : methods) {
            // 获取每个方法的注解
            final Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 循环方法的每个注解，找到符合的类型并解析
                final Class<? extends Annotation> type = annotation.annotationType();
                final BaseEvent baseEvent;
                if (type != null && (baseEvent = type.getAnnotation(BaseEvent.class)) != null) {
                    parseBaseEvent(host, method, annotation, type, baseEvent);
                }
            }
        }
    }

    private static void parseBaseEvent(@NonNull Object host, Method method, Annotation annotation,
                                       Class<? extends Annotation> type, BaseEvent baseEvent) throws Exception {
        // eventSetter: setOnClickListener()
        final String eventSetter = baseEvent.eventSetter();
        // eventCls: View.OnClickListener.class
        final Class<?> eventCls = baseEvent.eventCls();
        // eventCallback: onClick()
        final String eventCallback = baseEvent.eventCallback();
        final EventInvokeHandler invokeHandler = new EventInvokeHandler(host);
        invokeHandler.methodArray.put(eventCallback, method);
        final Object eventListener = Proxy.newProxyInstance(
                host.getClass().getClassLoader(), new Class[]{eventCls}, invokeHandler);
        // views: R.id.xxa, R.id.xxb, R.id.xxc
        final int[] value = (int[]) type.getDeclaredMethod("value").invoke(annotation);
        for (int id : value) {
            // throws NoSuchMethodException
            final Object view = host.getClass().getMethod("findViewById", int.class).invoke(host, id);
            if (view != null) {
                view.getClass().getMethod(eventSetter, eventCls).invoke(view, eventListener);
            }
        }
    }

    private static class EventInvokeHandler implements InvocationHandler {

        private static final String TAG = "EventInvokeHandler";

        final Object target;
        final Map<String, Method> methodArray = new ArrayMap<>();
        // 连续事件拦截
        final SparseLongArray times = new SparseLongArray();

        private EventInvokeHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final Object arg = args0OrNull(args);
            final int key = arg == null ? method.hashCode() : arg.hashCode();
            if (times.indexOfKey(key) >= 0) {
                if (System.currentTimeMillis() - times.get(key) < 1000) {
                    return null;
                }
            }
            method = methodArray.get(method.getName());
            if (method != null && target != null) {
                Log.d(TAG, "invoke() called with: proxy = [" + target.getClass().getSimpleName() + "], method = ["
                        + method.getName() + "], args = [" + arg + "]");
                method.setAccessible(true);
                times.put(key, System.currentTimeMillis());
                return method.invoke(target);
            }
            return null;
        }

        private Object args0OrNull(Object[] args) {
            return args == null || args.length == 0 ? null : args[0];
        }
    }
}
