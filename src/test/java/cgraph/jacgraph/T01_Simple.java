package cgraph.jacgraph;

import cgraph.jacgraph.entity.CStatus;
import cgraph.jacgraph.sdk.GNode;
import cgraph.jacgraph.sdk.GPipeline;
import cgraph.jacgraph.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Choo.lch
 * @date 2025/6/2 20:13
 * @Description:
 */
public class T01_Simple {

    public static void main(String[] args) throws InterruptedException {

        GPipeline pipeline = new GPipeline();
        MyNode1 a = new MyNode1();
        MyNode2 b = new MyNode2();
        MyNode1 c = new MyNode1();
        MyNode2 d = new MyNode2();

        pipeline.registerGElement(a, new ArrayList<>(), "nodeA", 1);
        pipeline.registerGElement(b, Arrays.asList(a), "nodeB", 1);
        pipeline.registerGElement(c, Arrays.asList(a), "nodeC", 1);
        pipeline.registerGElement(d, Arrays.asList(b, c), "nodeD", 1);

        pipeline.process(1);
    }


    public static class MyNode1 extends GNode{
        @Override
        public CStatus run() throws InterruptedException {
            System.out.println(String.format("[%s] [%s] enter MyNode1 run function. Sleep for 1 second...", DateUtils.toLocalString(DateUtils.FORMAT), this.getName()));
            Thread.sleep(1000);
            return new CStatus();
        }
    }

    public static class MyNode2 extends GNode{
        @Override
        public CStatus run() throws InterruptedException {
            System.out.println(String.format("[%s] [%s] enter MyNode2 run function. Sleep for 2 second...", DateUtils.toLocalString(DateUtils.FORMAT), this.getName()));
            Thread.sleep(2000);
            return new CStatus();
        }
    }

}