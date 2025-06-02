package cgraph.jacgraph.enums;

/**
 * @author chunqi.lch
 * @date 2025/6/2 17:52
 * @Description:
 */
public enum ErrorCode {

    FAIL(-1, "common_error"),
    SUC(0, "common_success"),
    ;

    private Integer code;
    private String desc;

    ErrorCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
