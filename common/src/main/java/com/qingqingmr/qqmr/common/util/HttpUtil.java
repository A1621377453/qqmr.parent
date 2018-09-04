package com.qingqingmr.qqmr.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @author ztl
 * @datetime 2018-07-13 15:05:15
 */
public final class HttpUtil {
    private final static Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 基于CloseableHttpClient，发送httpPost请求xml数据，返回请求结果
     *
     * @param url
     * @param xml
     * @return
     */
    public static String requestPost(String url, String xml) {
        CloseableHttpClient httpclient = null;
        if (url.endsWith("https")) {
            SSLConnectionSocketFactory sslConnectionSocketFactory = null;
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;
            try {
                SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                });
                sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), new String[]{"SSLv2", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
                Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new PlainConnectionSocketFactory())
                        .register("https", sslConnectionSocketFactory)
                        .build();
                poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
                poolingHttpClientConnectionManager.setMaxTotal(200);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("请求配置参数异常--{}--{}", url, e.getMessage());
                return null;
            }
            httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setConnectionManager(poolingHttpClientConnectionManager).setConnectionManagerShared(true).build();
        } else {
            httpclient = HttpClientBuilder.create().build();
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
        String result = "";
        try {
            StringEntity stringEntity = new StringEntity(xml, "UTF-8");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            LOG.info("response -- {}", response.toString());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            LOG.info("result --{}", result);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("请求异常--{}--{}", url, e.getMessage());
            return null;
        } finally {
            httpPost.releaseConnection();
        }

        return result;
    }

    /**
     * http请求，基于HttpURLConnection
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            // 往服务器端写内容 也就是发起http请求需要带的参数
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            // 读取服务器端返回的内容
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("请求异常--{}--{}--{}", requestUrl, requestMethod, e.getMessage());
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 发送post请求，基于CloseableHttpClient
     *
     * @param url
     * @param map
     * @return
     */
    public static byte[] requestPost(String url, Map<String, Object> map) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        byte[] bytes = null;
        try {
            StringEntity stringEntity = new StringEntity(FastJsonUtil.toJsonString(map), "UTF-8");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            LOG.info("response -- {}", response.toString());
            HttpEntity entity = response.getEntity();
            bytes = EntityUtils.toByteArray(entity);
            // LOG.info("result --{}", Base64.encodeBase64String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("请求异常--{}--{}", url, e.getMessage());
            return null;
        } finally {
            httpPost.releaseConnection();
        }

        return bytes;
    }

    /**
     * 验证证书公共http请求方法
     *
     * @param url
     * @param data
     * @param zfpath 证书的路径
     * @param mchid  商户id
     * @return
     * @author ztl
     */
    public static String wxHttpPost(String url, String data, String zfpath, String mchid) {
        KeyStore keyStore = null;
        try (FileInputStream fis = new FileInputStream(new File(zfpath))) {
            // 指定读取证书格式为PKCS12
            keyStore = KeyStore.getInstance("PKCS12");
            // 加载证书
            keyStore.load(fis, mchid.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("加载证书异常--{}--{}--{}", url, data, e.getMessage());
            return null;
        }

        StringBuffer message = new StringBuffer();
        CloseableHttpResponse response = null;
        try {
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
            // Allow TLSv1 protocol only
            // BrowserCompatHostnameVerifier已经过期，不在使用
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(null);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, hostnameVerifier);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Host", "api.mch.weixin.qq.com");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Cache-Control", "max-age=0");
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    message.append(text);
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("请求异常--{}--{}--{}", url, data, e.getMessage());
            return null;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return message.toString();
    }
}
