package cgraph.jacgraph.sdk;

import cgraph.jacgraph.entity.CStatus;
import cgraph.jacgraph.enums.ErrorCode;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Choo.lch
 * @date 2025/6/2 17:58
 * @Description:
 */
public abstract class GElement {

    protected HashSet<GElement> runBefore = new HashSet<>();
    protected HashSet<GElement> dependence = new HashSet<>();

    private String name = "";
    private int loop = 1;
    private AtomicInteger leftDependCounter = new AtomicInteger(0);

    private GParamManager paramManager;

    protected CStatus init() {
        return new CStatus();
    }

    protected CStatus destroy() {
        return new CStatus();
    }

    protected CStatus addElementInfo(List<GElement> depends, String name, int loop, GParamManager paramManager) {
        this.name = name;
        this.loop = loop;
        this.paramManager = paramManager;

        // 记录节点依赖
        if (null != depends && !depends.isEmpty()) {
            for (GElement depend : depends) {
                //todo 节点循环依赖校验
                dependence.add(depend);
                depend.runBefore.add(this);
            }
            resetDepend();
        }

        return new CStatus();
    }

    protected void resetDepend(){
        this.leftDependCounter = new AtomicInteger(dependence.size());
    }

    protected boolean decrementDepend() {
        return this.leftDependCounter.addAndGet(-1) == 0;
    }

    public abstract CStatus run() throws InterruptedException;

    protected CStatus fatRun() throws InterruptedException {
        for (int i = 0; i < loop; i++) {
            CStatus runStatus = this.run();
            if (!runStatus.isOk()) {
                return runStatus;
            }
        }
        return new CStatus();
    }

    public String getName(){
        return this.name;
    }

    public CStatus createGParam(GParam param, String key) {
        if (null == paramManager) {
            return new CStatus(ErrorCode.FAIL.getCode(), "paramManager is null");
        }
        return paramManager.create(param, key);
    }

    public GParam getGParam(String key) {
        if (null == paramManager) {
            return null;
        }
        return paramManager.get(key);
    }

}
