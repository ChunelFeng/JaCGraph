package cgraph.jacgraph.entity;

import cgraph.jacgraph.enums.ErrorCode;

/**
 * @author Choo.lch
 * @date 2025/6/2 17:50
 * @Description:
 */
public class CStatus {

    /**
     * 错误码 {@link ErrorCode}
     */
    private Integer errorCode = ErrorCode.SUC.getCode();

    /**
     * 错误信息
     */
    private String errorInfo;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean isOk() {
        return this.errorCode.compareTo(ErrorCode.SUC.getCode()) == 0;
    }

    public void reset(CStatus status) {
        if (null==status || status.isOk()) {
            return;
        }
        this.setErrorCode(status.getErrorCode());
        this.setErrorInfo(status.getErrorInfo());
    }

}
