package cgraph.jacgraph.sdk;

import cgraph.jacgraph.entity.CStatus;

/**
 * @author Choo.lch
 * @date 2025/6/22 16:19
 * @Description:
 */
public abstract class GParam {

    public CStatus setup() {
        return new CStatus();
    }


    public void reset(CStatus curStatus) {
    }


}
