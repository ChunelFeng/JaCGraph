package cgraph.jacgraph.sdk;

import cgraph.jacgraph.entity.CStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Choo.lch
 * @date 2025/6/22 16:33
 * @Description:
 */
public class GParamManager {

    protected Map<String, GParam> params_ = new ConcurrentHashMap();

    public <T extends GParam> CStatus create(T param, String key) {
        if (!params_.containsKey(key)) {
            params_.put(key, param);
        }
        return new CStatus();
    }

    public GParam get(String key) {
        return params_.get(key);
    }

    protected CStatus setup() {
        final CStatus status = new CStatus();
        params_.values().forEach(p -> status.reset(p.setup()));
        return status;
    }

    protected void reset(CStatus curStatus) {
        params_.values().forEach(p -> p.reset(curStatus));
    }

}
