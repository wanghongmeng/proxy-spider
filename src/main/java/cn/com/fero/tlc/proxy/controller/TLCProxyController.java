package cn.com.fero.tlc.proxy.controller;

import cn.com.fero.tlc.proxy.common.TLCProxyConstants;
import cn.com.fero.tlc.proxy.logger.TLCProxyLogger;
import cn.com.fero.tlc.proxy.vo.ResponseValue;
import cn.com.fero.tlc.proxy.vo.TLCProxy;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Queue;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
@RestController
public class TLCProxyController {
    @Resource
    private Queue<String> httpProxy;
    @Resource
    private Queue<String> httpsProxy;

    @RequestMapping(value = "/proxy/http")
    public ResponseValue getHttpProxy() {
        try {
            if (httpProxy.isEmpty()) {
                return constructNotFoundResponse();
            }

            return constructSuccessResponse(httpProxy.peek());
        } catch (Exception e) {
            TLCProxyLogger.getLogger().error(ExceptionUtils.getFullStackTrace(e));
            return constructErrorResponse();
        }
    }

    @RequestMapping(value = "/proxy/https")
    public ResponseValue getHttpsProxy() {
        try {
            if (httpsProxy.isEmpty()) {
                return constructNotFoundResponse();
            }

            return constructSuccessResponse(httpsProxy.peek());
        } catch (Exception e) {
            TLCProxyLogger.getLogger().error(ExceptionUtils.getFullStackTrace(e));
            return constructErrorResponse();
        }
    }

    private ResponseValue constructNotFoundResponse() {
        ResponseValue responseValue = new ResponseValue();
        responseValue.setStatus(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_NOT_FOUND);
        responseValue.setMessage(TLCProxyConstants.SPIDER_CONST_RESPONSE_MESSAGE_NOT_FOUND);
        return responseValue;
    }

    private ResponseValue constructSuccessResponse(String proxy) {
        ResponseValue responseValue = new ResponseValue();
        responseValue.setStatus(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_SUCCESS);
        String[] proxyArray = proxy.split(TLCProxyConstants.SPIDER_CONST_COLON);
        String ip = proxyArray[0];
        String port = proxyArray[1];
        responseValue.setProxy(new TLCProxy(ip, Integer.parseInt(port)));
        return responseValue;
    }

    private ResponseValue constructErrorResponse() {
        ResponseValue responseValue = new ResponseValue();
        responseValue.setStatus(TLCProxyConstants.SPIDER_CONST_RESPONSE_STATUS_ERROR);
        responseValue.setMessage(TLCProxyConstants.SPIDER_CONST_RESPONSE_MESSAGE_ERROR);
        return responseValue;
    }
}
