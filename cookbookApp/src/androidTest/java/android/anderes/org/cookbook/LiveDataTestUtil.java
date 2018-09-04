package android.anderes.org.cookbook;

import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {
    public static < T > T getValue(final LiveData< T > liveData, int counter) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(counter);
        Observer < T > observer = new Observer< T >() {
            @Override
            public void onChanged(@Nullable T o) {
                Log.v("Testing", "--------------- o.status: " + ((Resource)o).status);
                data[0] = o;
                latch.countDown();
                if (latch.getCount() <= 0) {
                    liveData.removeObserver(this);
                }
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }
}
