package cn.com.fero.tlc.proxy.controller;

import cn.com.fero.tlc.proxy.vo.ResponseValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wanghongmeng on 2015/7/17.
 */
@RestController
public class TLCProxyController {
    @RequestMapping(value = "/proxy/http")
    public ResponseValue getHttpProxy() {
        return new ResponseValue();
    }

    @RequestMapping(value = "/proxy/https")
    public ResponseValue getHttpsProxy() {
        return new ResponseValue();
    }
}
