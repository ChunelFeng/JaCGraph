package cgraph.jacgraph;

import cgraph.jacgraph.entity.CStatus;
import cgraph.jacgraph.sdk.GNode;
import cgraph.jacgraph.sdk.GParam;
import cgraph.jacgraph.sdk.GPipeline;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Choo.lch
 * @date 2025/6/22 17:23
 * @Description:
 */
public class T05_Param {

    private static final String kParamKey = "paramKey";

    public static void main(String[] args) throws InterruptedException {

        GPipeline pipeline = new GPipeline();

        MyWriteParamNode w = new MyWriteParamNode();
        MyReadParamNode r = new MyReadParamNode();

        pipeline.registerGElement(w, new ArrayList<>(), "WriteNode", 1);
        pipeline.registerGElement(r, Arrays.asList(w), "ReadNode", 1);

        pipeline.process(5);
    }

    public static class MyParam extends GParam {
        public int val = 0;
        public int loop = 0;

        @Override
        public void reset(CStatus curStatus) {
            this.val = 0;
        }
    }

    public static class MyReadParamNode extends GNode {
        @Override
        public CStatus run() {
            MyParam myParam = (MyParam) getGParam(kParamKey);
            System.out.printf("%s: val = %d, loop = %d %n", getName(), myParam.val, myParam.loop);
            return new CStatus();
        }
    }

    public static class MyWriteParamNode extends GNode {
        @Override
        protected CStatus init() {
            return createGParam(new MyParam(), kParamKey);
        }

        @Override
        public CStatus run() {
            MyParam myParam = (MyParam) getGParam(kParamKey);
            myParam.val += 1;
            myParam.loop += 1;
            System.out.printf("%s: val = %d, loop = %d %n", getName(), myParam.val, myParam.loop);
            return new CStatus();
        }
    }
}