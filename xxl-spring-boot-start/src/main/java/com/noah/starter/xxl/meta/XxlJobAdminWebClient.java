package com.noah.starter.xxl.meta;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class XxlJobAdminWebClient {

    private OkHttpClient xxlJobOkHttpClient;

    private String username;

    private String password;

    private String adminAddress;

    private String cookie;

    public synchronized String getCookie() {

        if (StrUtil.isNotEmpty(cookie)) {
            return cookie;
        }

        Map<String, String> map = Maps.newHashMap();
        map.put("username", username);
        map.put("password", DigestUtils.md5DigestAsHex(password.getBytes()));

        String tokenJson = JSONUtil.toJsonStr(map);
        String cookie = new BigInteger(tokenJson.getBytes()).toString(16);
        this.cookie = cookie;

        return cookie;
    }

    public Long getJobGroupId(String name) {
        JsonArray jsonArray = pageList("jobgroup", "appname=" + name);
        for (JsonElement jsonElement : jsonArray) {
            String appname = jsonElement.getAsJsonObject().get("appname").getAsString();
            if (!StringUtils.equals(appname, name)) {
                continue;
            }
            Long id = jsonElement.getAsJsonObject().get("id").getAsLong();
            return id;
        }
        return null;
    }

    public void saveJobGroup(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("appname", name);
        param.put("title", name);
        param.put("addressType", "0");
        param.put("addressList", "");
        save("jobgroup/save", param);
    }

    public void saveJobInfo(Long jobGroupId, String remark, String cron, String handler) {
        Map<String, String> param = new HashMap<>();
        param.put("jobGroup", jobGroupId.toString());
        param.put("jobDesc", remark);
        param.put("author", "AutoRegister");
        param.put("scheduleType", "CRON");
        param.put("scheduleConf", cron);
        param.put("cronGen_display", cron);
        param.put("schedule_conf_CRON", cron);
        param.put("glueType", "BEAN");
        param.put("executorHandler", handler);
        param.put("executorRouteStrategy", "ROUND");
        param.put("misfireStrategy", "DO_NOTHING");
        param.put("executorBlockStrategy", "SERIAL_EXECUTION");
        param.put("executorTimeout", "0");
        param.put("executorFailRetryCount", "0");
        param.put("glueRemark", "GLUE代码初始化");
        param.put("triggerStatus", "1");
        save("jobinfo/add", param);
    }


    public Long getJobInfoId(Long jobGroupId, String executorHandler) {
        Map<String, String> data = new HashMap<>();
        data.put("jobGroup", String.valueOf(jobGroupId));
        data.put("executorHandler", executorHandler);
        data.put("triggerStatus", "-1");
        String str = buildQuery(data);
        JsonArray jsonArray = pageList("jobinfo", str);
        for (JsonElement jsonElement : jsonArray) {
            String resExecutorHandler = jsonElement.getAsJsonObject().get("executorHandler").getAsString();
            if (!StringUtils.equals(resExecutorHandler, executorHandler)) {
                continue;
            }
            Long id = jsonElement.getAsJsonObject().get("id").getAsLong();
            return id;
        }
        return null;
    }

    @SneakyThrows
    private JsonArray pageList(String type, String queryData) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, queryData + "&start=0&length=10");
        String url = String.format("%s/%s/pageList", adminAddress, type);
        Request request = new Request.Builder().url(url).post(body).addHeader("Cookie", "XXL_JOB_LOGIN_IDENTITY=" + getCookie()).build();
        Response response = xxlJobOkHttpClient.newCall(request).execute();
        String string = response.body().string();
        JsonElement jsonElement = JsonParser.parseString(string);
        return jsonElement.getAsJsonObject().get("data").getAsJsonArray();
    }

    @SneakyThrows
    private void save(String type, Map<String, String> param) {
        String data = buildQuery(param);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, data);
        String url = String.format("%s/%s", adminAddress, type);
        Request request = new Request.Builder().url(url).post(body).addHeader("Cookie", "XXL_JOB_LOGIN_IDENTITY=" + getCookie()).build();
        Response response = xxlJobOkHttpClient.newCall(request).execute();
        Validate.isTrue(response.isSuccessful(), "xxl-job-admin save " + type + " error");
    }

    private String buildQuery(Map<String, String> param) {
        return param.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), encodeUrl(entry.getValue()))).collect(Collectors.joining("&"));
    }

    @SneakyThrows
    private String encodeUrl(String s) {
        return URLEncoder.encode(s, "UTF-8");
    }

}
