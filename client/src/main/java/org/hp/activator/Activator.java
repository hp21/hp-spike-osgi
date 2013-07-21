/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.hp.activator;

import java.util.concurrent.atomic.AtomicBoolean;

import org.hp.maven.api.IServicea;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private Thread thread;
    private AtomicBoolean stop = new AtomicBoolean(false);

    public void start(final BundleContext context) {
        System.out.println("Starting the bundle: " + context.getBundle().getSymbolicName());

        try {
            stop.getAndSet(false);

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!stop.get()) {

                        ServiceTracker tracker = new ServiceTracker(context, IServicea.class, null);
                        tracker.open();
                        IServicea service = (IServicea) tracker.getService();

                        if (null != service) {
                            System.out.println(service.echo("Honap halnap ***"));
                        } else {
                            System.out.println("Service not found");
                        }

                        tracker.close();

                        sleep();
                    }
                }

                private void sleep() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } finally {

        }
    }

    public void stop(BundleContext context) {
        stop.set(true);
        System.out.println("Stopping the bundle: " + context.getBundle().getSymbolicName());
    }

}
