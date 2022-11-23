/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.jsfdescriptor;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassLevelAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassLevelAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyJsfClientBase;
import jakarta.ejb.ConcurrentAccessTimeoutException;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBs;
import java.io.Serializable;
import java.util.List;

@EJBs({
        @EJB(name = AccessTimeoutIF.beanClassLevelAccessTimeoutBeanLocal, beanName = "BeanClassLevelAccessTimeoutBean", beanInterface = AccessTimeoutIF.class)
})
@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient extends StatefulConcurrencyJsfClientBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * testName: beanClassLevel
     *
     * @test_Strategy:
     */
    public void beanClassLevel() throws InterruptedException {
        beanClassLevel(getBeanClassLevelAccessTimeoutBeanLocal());
    }

    protected void beanClassLevel(final AccessTimeoutIF b) throws InterruptedException {
        List<Exception> exceptionList = concurrentPing(new Runnable() {
            public void run() {
                b.beanClassLevel();
            }
        });
        checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
    }

    /*
     * testName: beanClassLevel2
     *
     * @test_Strategy:
     */
    public void beanClassLevel2() throws InterruptedException {
        beanClassLevel2(getBeanClassLevelAccessTimeoutBeanLocal());
    }

    protected void beanClassLevel2(final AccessTimeoutIF b) throws InterruptedException {
        List<Exception> exceptionList = concurrentPing(new Runnable() {
            public void run() {
                b.beanClassLevel2();
            }
        });
        checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
    }

    /*
     * @testName: pingMethodInBeanSuperClass
     *
     * @test_Strategy: ejb-jar.xml declares <concurrent-method> and their <access-timeout>
     */
    public void pingMethodInBeanSuperClass() throws Exception {
        final AccessTimeoutIF b = getBeanClassLevelAccessTimeoutBeanLocal();
        List<Exception> exceptionList = concurrentPing(new Runnable() {
            public void run() {
                b.ping();
            }
        });
        checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
    }

    /*
     * @testName: beanClassMethodLevel
     *
     * @test_Strategy: beanClassMethodLevel is a concurrent method with default access-timeout. It is not declared in
     * ejb-jar.xml with <concurrent-method>
     */
    public void beanClassMethodLevel() throws InterruptedException {
        final AccessTimeoutIF b = getBeanClassLevelAccessTimeoutBeanLocal();
        List<Exception> exceptionList = concurrentPing(new Runnable() {
            public void run() {
                b.beanClassMethodLevel();
            }
        });
        checkConcurrentAccessTimeoutResult(exceptionList, CONCURRENT_INVOCATION_TIMES, 0);
    }

    // remote view tests are only available in JavaEE profile
    protected AccessTimeoutIF getBeanClassMethodLevelOverrideAccessTimeoutBeanLocal() {
        return (AccessTimeoutIF) lookup(beanClassMethodLevelOverrideAccessTimeoutBeanLocal, null, null);
    }

    protected AccessTimeoutIF getBeanClassMethodLevelAccessTimeoutBeanLocal() {
        return (AccessTimeoutIF) lookup(beanClassMethodLevelAccessTimeoutBeanLocal, null, null);
    }

    protected AccessTimeoutIF getBeanClassLevelAccessTimeoutBeanLocal() {
        return (AccessTimeoutIF) lookup(beanClassLevelAccessTimeoutBeanLocal, null, null);
    }

    protected AccessTimeoutIF getAnnotatedSuperClassAccessTimeoutBeanLocal() {
        return (AccessTimeoutIF) lookup(annotatedSuperClassAccessTimeoutBeanLocal, null, null);
    }

    protected AccessTimeoutIF getBeanClassMethodLevelOverrideAccessTimeoutBeanRemote() {
        return (AccessTimeoutIF) lookup(beanClassMethodLevelOverrideAccessTimeoutBeanRemote, null, null);
    }

    protected AccessTimeoutIF getBeanClassMethodLevelAccessTimeoutBeanRemote() {
        return (AccessTimeoutIF) lookup(beanClassMethodLevelAccessTimeoutBeanRemote, null, null);
    }

    protected AccessTimeoutIF getBeanClassLevelAccessTimeoutBeanRemote() {
        return (AccessTimeoutIF) lookup(beanClassLevelAccessTimeoutBeanRemote, null, null);
    }

    protected AccessTimeoutIF getAnnotatedSuperClassAccessTimeoutBeanRemote() {
        return (AccessTimeoutIF) lookup(annotatedSuperClassAccessTimeoutBeanRemote, null, null);
    }

    protected void checkConcurrentAccessTimeoutResult(
            List<Exception> exceptionList, int nullCountExpected, int concurrentAccessTimeoutExceptionCountExpected) {
        int nullCount = 0;
        int concurrentAccessTimeoutExceptionCount = 0;
        for (Exception e : exceptionList) {
            if (e == null) {
                appendReason("Got no exception, which may be correct.");
                nullCount++;
            } else if (e instanceof ConcurrentAccessTimeoutException) {
                appendReason("Got ConcurrentAccessTimeoutException, which may be correct: ", e);
                concurrentAccessTimeoutExceptionCount++;
            } else {
                throw new RuntimeException("Expecting null or ConcurrentAccessTimeoutException, but got ", e);
            }
        }
        assertEquals("Check nullCount", nullCountExpected, nullCount);
        assertEquals(
                "Check concurrentAccessExceptionCount",
                concurrentAccessTimeoutExceptionCountExpected,
                concurrentAccessTimeoutExceptionCount);
    }
}
