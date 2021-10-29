/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.lyloou.component.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Application keeper.
 * ref: https://github.com/seata/seata-samples
 *
 * @author lilou
 */
@Slf4j
public class ApplicationKeeper {


    private final ReentrantLock LOCK = new ReentrantLock();
    private final Condition STOP = LOCK.newCondition();

    /**
     * Instantiates a new Application keeper.
     *
     * @param applicationContext the application context
     */
    public ApplicationKeeper(AbstractApplicationContext applicationContext) {
        addShutdownHook(applicationContext);
    }

    private void addShutdownHook(final AbstractApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    applicationContext.close();
                    log.info("ApplicationContext " + applicationContext + " is closed.");
                } catch (Exception e) {
                    log.error("Failed to close ApplicationContext", e);
                }

                LOCK.lock();
                try {
                    STOP.signal();
                } finally {
                    LOCK.unlock();
                }
            }
        }));
    }

    /**
     * Keep.
     */
    public static void keep(AbstractApplicationContext context) {
        new ApplicationKeeper(context).await();
    }

    private void await() {
        LOCK.lock();
        try {
            log.info("Application is keep running ... ");
            STOP.await();
        } catch (InterruptedException e) {
            log.error("ApplicationKeeper.keep() is interrupted by InterruptedException!", e);
        } finally {
            LOCK.unlock();
        }
    }
}
