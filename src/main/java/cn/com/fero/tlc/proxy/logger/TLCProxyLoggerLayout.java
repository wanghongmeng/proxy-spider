package cn.com.fero.tlc.proxy.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import org.apache.commons.lang3.time.DateFormatUtils;


/**
 * Created by wanghongmeng on 2015/7/20.
 */
public class TLCProxyLoggerLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuffer sb = new StringBuffer(256);
        sb.append(DateFormatUtils.format(event.getTimeStamp(), TLCProxyConstants.SPIDER_CONST_DATE_TIME_FORMAT));
        sb.append(" - ");
        sb.append("[").append(event.getThreadName()).append("]");
        sb.append(" - ");
        sb.append("[").append(event.getLevel()).append("]");
        sb.append(" - ");
        sb.append("[").append(event.getLoggerName()).append("]");
        sb.append(" - ");
        sb.append(event.getFormattedMessage());
        sb.append(" - ");
        sb.append(event.getThrowableProxy());
        sb.append(CoreConstants.LINE_SEPARATOR);
        return sb.toString();
    }
}