package com.qingqingmr.qqmr.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 自定义业务异常
 * </p>
 *
 * @author ztl
 * @datetime 2018年7月8日 上午10:26:33
 */
public class ServiceRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -8583290390866454370L;
    private final static Logger LOG = LoggerFactory.getLogger(ServiceRuntimeException.class);

    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message, boolean isRollback) {
        super(message);
        this.isRollback = isRollback;
    }

    @Override
    public String getMessage() {
        LOG.error(super.getMessage() + (this.isRollback ? " ,事务回滚" : " ,事务不回滚"));
        return super.getMessage();
    }

    /**
     * 是否事务回滚
     */
    private boolean isRollback;

    public boolean isRollback() {
        return isRollback;
    }

    public void setRollback(boolean isRollback) {
        this.isRollback = isRollback;
    }
}
