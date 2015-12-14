package cn.com.fero.util.proxy.vo;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
public class ResponseValue {
    private int status;
    private String message;
    private TLCProxy proxy;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TLCProxy getProxy() {
        return proxy;
    }

    public void setProxy(TLCProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseValue that = (ResponseValue) o;

        if (status != that.status) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (proxy != null ? !proxy.equals(that.proxy) : that.proxy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (proxy != null ? proxy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseValue{");
        sb.append("status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append(", proxy=").append(proxy);
        sb.append('}');
        return sb.toString();
    }
}
