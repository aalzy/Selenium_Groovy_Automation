/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package gls.invocation

import org.codehaus.groovy.control.CompilationFailedException
import org.junit.Test

import static groovy.test.GroovyAssert.assertScript
import static groovy.test.GroovyAssert.shouldFail

final class ConstructorDelegationTest {

    @Test
    void testThisCallWithParameter() {
        assertScript '''
            class A {
                def foo
                A(String x){foo=x}
                A(){this("bar")}
            }
            def a = new A()
            assert a.foo == "bar"
        '''
    }

    @Test
    void testThisCallWithoutParameter() {
        assertScript '''
            class A {
                def foo
                A(String x){this(); foo=x}
                A(){foo="bar"}
            }
            def a = new A("foo")
            assert a.foo == "foo"
        '''
    }

    @Test
    void testThisConstructorCallNotOnFirstStmt() {
        shouldFail CompilationFailedException, '''
            class ThisConstructorCall {
                public ThisConstructorCall() {
                    println 'dummy first statement'
                    this(19)
                }
                public ThisConstructorCall(int b) {
                    println 'another dummy statement'
                }
            }
            1
        '''
    }

    @Test
    void testSuperConstructorCallNotOnFirstStmt() {
        shouldFail CompilationFailedException, '''
            class SuperConstructorCall {
                public SuperConstructorCall() {
                    println 'dummy first statement'
                    super()
                }
            }
            1
        '''
    }

    @Test
    void testConstructorDelegationWithThisOrSuperInArgs() {
        // all 4 cases below were compiling earlier but giving VerifyError at runtime

        shouldFail CompilationFailedException, '''
            class MyClosure3128V1 extends Closure {
                MyClosure3128V1() {
                    super(this)
                }
                void run() { println 'running' }
            }
        '''

        shouldFail CompilationFailedException, '''
            class MyClosure3128V2 extends Closure {
                MyClosure3128V2() {
                    super(super)
                }
                void run() { println 'running' }
            }
        '''

        shouldFail CompilationFailedException, '''
            class MyClosure3128V3 extends Closure {
                MyClosure3128V3() {
                    this(this)
                }
                MyClosure3128V3(owner) {}
                void run() { println 'running' }
            }
        '''

        shouldFail CompilationFailedException, '''
            class MyClosure3128V4 extends Closure {
                MyClosure3128V4() {
                    this(super)
                }
                MyClosure3128V4(owner) {}
                void run() { println 'running' }
            }
        '''
    }

    @Test // GROOVY-6618
    void testVariadicConstructor() {
        assertScript '''
            class Foo {
                public info
                Foo(String s,Integer[] a){info=a}
                Foo() {this("foo",1)}
            }
            assert new Foo().info == [1]
        '''

        assertScript '''
            class Foo {
                public info
                Foo(String s,Integer[] a){info=a}
                Foo() {this("foo",null)}
            }
            assert new Foo().info == null
        '''

        assertScript '''
            class Foo {
                public info
                Foo(String s,Integer[] a){info=a}
                Foo() {this("foo",1,2,3)}
            }
            assert new Foo().info == [1,2,3]
        '''

        assertScript '''
            class Foo {
                public info
                Foo(String s,Integer[] a){info=a}
                Foo() {this("foo",new Integer[]{1,2,3})}
            }
            assert new Foo().info == [1,2,3]
        '''

        assertScript '''
            class Foo {
                public info
                Foo(String s,Integer[] a){info=a}
                Foo() {this("foo")}
            }
            assert new Foo().info == []
        '''
    }
}
