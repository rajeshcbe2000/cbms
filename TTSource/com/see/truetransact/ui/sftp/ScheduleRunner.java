/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ScheduleRunner.java
 */
package com.see.truetransact.ui.sftp;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.naming.Context;

/**
 * Run a simple task once every second, starting 3 seconds from now. Cancel the
 * task after 20 seconds.
 */
public final class ScheduleRunner {

    /**
     * Run the example.
     */
    public static void main(String... aArgs) throws InterruptedException {
        log("Main started.");
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
        }
        /*
         Seconds*60
         *mins60
         *hrs24
         *days365
         *years10*/
        ScheduleRunner alarmClock = new ScheduleRunner(0, 300, 315360000);//(0, 300, 21600);(60*60*24*365*10) 
        System.out.println("started");
        alarmClock.activateAlarmThenStop();
        /*
         To start the alarm at a specific date in the future, the initial delay
         needs to be calculated relative to the current time, as in :
         Date futureDate = ...
         long startTime = futureDate.getTime() - System.currentTimeMillis();
         AlarmClock alarm = new AlarmClock(startTime, 1, 20);
         This works only if the system clock isn't reset.
         */
        log("Main ended.");
    }

    ScheduleRunner(long aInitialDelay, long aDelayBetweenBeeps, long aStopAfter) {
        fInitialDelay = aInitialDelay;
        fDelayBetweenRuns = aDelayBetweenBeeps;
        fShutdownAfter = aStopAfter;
        fScheduler = Executors.newScheduledThreadPool(NUM_THREADS);
    }

    /**
     * Sound the alarm for a few seconds, then stop.
     */
    void activateAlarmThenStop() {
        Runnable soundAlarmTask = new SoundAlarmTask();
        ScheduledFuture<?> soundAlarmFuture = fScheduler.scheduleWithFixedDelay(
                soundAlarmTask, fInitialDelay, fDelayBetweenRuns, TimeUnit.SECONDS);
        Runnable stopAlarm = new StopAlarmTask(soundAlarmFuture);
        fScheduler.schedule(stopAlarm, fShutdownAfter, TimeUnit.SECONDS);

        System.out.println("end");
    }
// PRIVATE 
    private final ScheduledExecutorService fScheduler;
    private final long fInitialDelay;
    private final long fDelayBetweenRuns;
    private final long fShutdownAfter;

    private static void log(String aMsg) {
        System.out.println(aMsg);
    }
    /**
     * If invocations might overlap, you can specify more than a single thread.
     */
    private static final int NUM_THREADS = 1;
    private static final boolean DONT_INTERRUPT_IF_RUNNING = false;

    private static final class SoundAlarmTask implements Runnable {

        FTPSTest ftp = null;

        @Override
        public void run() {
            try {
                System.out.println("Inside SoundAlarmTask");
                ftp = new FTPSTest();
                System.out.println("FTPSTest object created...");
//  CommonUtil.getRemoteContext();

                //java.util.List list = ClientUtil.executeQuery("getServerTime", new HashMap());
                ftp.connectToFTPS(ftp, CommonUtil.convertObjToStr(CommonConstants.ELECTRONIC_TYPE_RTGSNEFT));
                ftp.connectToFTPS(ftp, CommonUtil.convertObjToStr(CommonConstants.ELECTRONIC_TYPE_ATM));
                //  ftp = new FTPSTest();//temp comment
                //ftp.connectToFTPS(ftp, "RTGS");

                System.out.println("FTP connected...");
                ++fCount;
                log("beep " + fCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private int fCount;
    }

    private final class StopAlarmTask implements Runnable {

        StopAlarmTask(ScheduledFuture<?> aSchedFuture) {
            fSchedFuture = aSchedFuture;
        }

        @Override
        public void run() {
            log("Stopping alarm.");
            fSchedFuture.cancel(DONT_INTERRUPT_IF_RUNNING);
            System.out.println("before shutdown print");
            /*
             Note that this Task also performs cleanup, by asking the
             scheduler to shutdown gracefully.
             */
            fScheduler.shutdown();
            System.exit(0);
        }
        private ScheduledFuture<?> fSchedFuture;
    }
}
