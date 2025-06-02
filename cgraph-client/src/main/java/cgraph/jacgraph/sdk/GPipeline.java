package cgraph.jacgraph.sdk;

import cgraph.jacgraph.entity.CStatus;
import cgraph.jacgraph.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author chunqi.lch
 * @date 2025/6/2 18:09
 * @Description:
 */
public class GPipeline {

    private List<GElement> elements = new ArrayList<>();
    private AtomicInteger finishedElementSize = new AtomicInteger(0);
    private CStatus cStatus = new CStatus();

    /**
     * 注册元素信息
     * @param element 当前节点
     * @param depends 依赖节点
     * @param name 当前节点名称
     * @param loop 当前节点执行次数
     * @return
     * @param <T>
     */
    public <T extends GElement> CStatus registerGElement(T element, List<GElement> depends, String name, int loop){
        element.addElementInfo(depends, name, loop);
        elements.add(element);
        return new CStatus();
    }

    public CStatus init() {
        for (GElement element : elements) {
            CStatus status = element.init();
            if (ErrorCode.SUC.getCode().compareTo(status.getErrorCode()) != 0) {
                return status;
            }
        }
        return new CStatus();
    }

    public CStatus destroy() {
        for (GElement element : elements) {
            CStatus status = element.destroy();
            if (ErrorCode.SUC.getCode().compareTo(status.getErrorCode()) != 0) {
                return status;
            }
        }
        return new CStatus();
    }

    public CStatus run() throws InterruptedException {
        setup();
        executeAll();
        reset();
        return new CStatus();
    }

    public void setup() {
        resetFinishedElementSize();
        for (GElement element : elements) {
            element.resetDepend();
        }
    }

    private void executeAll() {
        for (GElement element : elements) {
            if (element.dependence.isEmpty()) {
                executeOne(element);
            }
        }
    }


    private void executeOne(GElement element){
        if (!cStatus.isOk()) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                cStatus.reset(element.fatRun());
                for (GElement afterElement : element.runBefore) {
                    boolean flag = afterElement.decrementDepend();
                    if (!flag) {
                        continue;
                    }
                    executeOne(afterElement);
                }
                synchronized (this) {
                    int currentSize = finishedElementSize.addAndGet(1);
                    if (currentSize == elements.size()) {
                        this.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                synchronized (this) {
                    this.notifyAll();
                }
                throw new RuntimeException(e);
            }
        });
    }

    private void reset() throws InterruptedException {
       synchronized (this){
           this.wait();
       }
    }

    private void resetFinishedElementSize(){
        this.finishedElementSize = new AtomicInteger(0);
    }

    public CStatus process(int runSize) throws InterruptedException {
        // init
        CStatus initStatus = init();
        if(!initStatus.isOk()){
            return initStatus;
        }

        // 多次执行，有一次失败，则失败
        CStatus runStatus = new CStatus();
        for (int i = 0; i < runSize; i++) {
            runStatus = run();
            if(!runStatus.isOk()){
                break;
            }
        }
        if(!runStatus.isOk()){
            destroy();
            return runStatus;
        }

        // destroy
        return destroy();
    }

}
