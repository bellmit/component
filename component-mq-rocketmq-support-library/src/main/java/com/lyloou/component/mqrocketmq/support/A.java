package com.lyloou.component.mqrocketmq.support;

/**
 * @author lilou
 * @since 2021/8/11
 */
public class A {

    public static void main(String[] args) {
        final D d = new D();
        System.out.println(d instanceof A);
    }
}

class B extends A {

}

class D extends B {

}
