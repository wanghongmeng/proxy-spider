package cn.com.fero.util.proxy.http.test;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by gizmo on 15/6/19.
 */
public class TLCProxyQueueTest extends TLCProxyTest {

    @Test
    public void testQueue() throws UnsupportedEncodingException {
        Queue<Integer> queue = new LinkedList();
        for (int a = 1; a <= 10; a++) {
            queue.add(a);
        }

//        Iterator<Integer> iterator = queue.iterator();
//        while (iterator.hasNext()) {
//            int num = iterator.next();
//            if (num % 3 == 0) {
//                iterator.remove();
//            } else {
//                System.out.println(num);
//            }
//        }
        if (queue.contains(11)) {
            System.out.println("right");
        } else {
            System.out.println("wrong");
        }
    }
}
