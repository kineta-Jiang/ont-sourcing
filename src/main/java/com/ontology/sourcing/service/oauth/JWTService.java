package com.ontology.sourcing.service.oauth;

import ch.qos.logback.classic.Logger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.ontology.sourcing.model.common.ResponseBean;
import com.ontology.sourcing.util.HttpUtil;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    //
    private Gson gson = new Gson();

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(JWTService.class);

    //
    String ontAuthJWTUrl;

    //
    public JWTService(@Value("${ont.auth.jwt.url}") String ontAuthJWTUrl) {
        this.ontAuthJWTUrl = ontAuthJWTUrl;
    }

    //
    public void verify(String access_token) throws Exception {
        //
        JSONObject obj = new JSONObject();
        obj.put("access_token", access_token);
        //
        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost(ontAuthJWTUrl, HttpUtil.JSON, obj);
        //
       try {
           ResponseBean response = gson.fromJson(httpInfo.responseBody, ResponseBean.class);
           boolean rst = (boolean) response.getResult();
           if (!rst){
               throw new Exception("verify failed.");
           }
       } catch (Exception e){
           //
           logger.error(e.getMessage());
           //
           throw new Exception(e.getMessage());
       }
    }

    //
    public String getContentUser(String access_token) throws JWTDecodeException {
        //
        DecodedJWT jwt = JWT.decode(access_token);
        // 只能输出String类型，如果是其他类型返回null
        return (String) jwt.getClaim("content").asMap().get("ontid");
    }
}
