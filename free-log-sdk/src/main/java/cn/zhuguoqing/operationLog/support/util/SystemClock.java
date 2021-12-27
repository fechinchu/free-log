package cn.zhuguoqing.operationLog.support.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存时间戳解决System.currentTimeMillis()高并发下性能问题<br>
 *     问题根源分析: http://pzemtsov.github.io/2017/07/23/the-slow-currenttimemillis.html
 *
 **/
public class SystemClock {

    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 尝试下枚举单例法
     */
    private enum SystemClockEnum {
        SYSTEM_CLOCK;
        private SystemClock systemClock;
        SystemClockEnum() {
            systemClock = new SystemClock(1);
        }
        public SystemClock getInstance() {
            return systemClock;
        }
    }

    /**
     * 获取单例对象
     * @return com.cmallshop.module.core.commons.util.sequence.SystemClock
     */
    private static SystemClock getInstance() {
        return SystemClockEnum.SYSTEM_CLOCK.getInstance();
    }

    /**
     * 获取当前毫秒时间戳
     * @return long
     */
    public static long now() {
        return getInstance().now.get();
    }

    /**
     * 起一个线程定时刷新时间戳
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "System Clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

}
