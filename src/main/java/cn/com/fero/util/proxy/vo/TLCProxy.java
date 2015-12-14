package cn.com.fero.util.proxy.vo;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
public class TLCProxy {
    private String ip;
    private int port;

    public TLCProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLCProxy that = (TLCProxy) o;

        if (port != that.port) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TLCProxyIp{");
        sb.append("ipUrl='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append('}');
        return sb.toString();
    }
}
