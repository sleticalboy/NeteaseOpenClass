package com.sleticalboy.ldbus;

import android.os.Looper;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 19-3-24.
 *
 * @author leebin
 */
public final class LiveDataBus {

    // channel -> dataSet
    private final Map<String, MutableLiveData<?>> channels;

    private LiveDataBus(Map<String, MutableLiveData<?>> channels) {
        this.channels = channels;
    }

    @SuppressWarnings("unchecked")
    private <T> MutableLiveData<T> getChannel(String channel) {
        MutableLiveData<?> liveData = channels.get(channel);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            channels.put(channel, liveData);
        }
        return (MutableLiveData<T>) liveData;
    }

    public <T> void postValue(String channel, T value) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            getChannel(channel).setValue(value);
        } else {
            getChannel(channel).postValue(value);
        }
    }

    public <T> void observe(String channel, LifecycleOwner owner, final Observer<T> observer) {
        getChannel(channel).observe(owner, obj -> {
            @SuppressWarnings("unchecked") final T t = (T) obj;
            observer.onChanged(t);
        });
    }

    private static class Singleton {
        static final LiveDataBus BUS = new LiveDataBus(new ConcurrentHashMap<>());
    }

    public static LiveDataBus get() {
        return Singleton.BUS;
    }
}
