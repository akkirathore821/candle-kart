//package com.candlekart.elasticsearch_service.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import org.apache.hc.client5.http.auth.AuthScope;
//import org.apache.hc.client5.http.auth.CredentialsProvider;
//import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
//import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
//import org.apache.hc.core5.http.HttpHost;
//import org.apache.hc.core5.ssl.SSLContextBuilder;
//import org.apache.hc.core5.ssl.SSLContexts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestClient;
//
//import javax.net.ssl.SSLContext;
//
//@Configuration
//public class ElasticsearchConfig {
//
//    @Value("${elasticsearch.host}")
//    private String host;
//
//    @Value("${elasticsearch.username}")
//    private String username;
//
//    @Value("${elasticsearch.password}")
//    private String password;
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient() throws Exception {
//
//        String serverUrl = "https://" + host;
//
//        SSLContextBuilder sslBuilder = SSLContexts.custom()
//                .loadTrustMaterial(null, (certificate, authType) -> true); // trust all
//        SSLContext sslContext = sslBuilder.build();
//
//        RestClient restClient1 = RestClient.builder(HttpHost.create(serverUrl))
//                .setHttpClientConfigCallback(hc -> hc
//                        .setSSLContext(sslContext)
//                        .setDefaultCredentialsProvider(credentials()))
//                .build();
//
//        RestClient restClient = RestClient.builder()
//
//        ElasticsearchTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//        return new ElasticsearchClient(transport);
//    }
//
//    private CredentialsProvider credentials() {
//        CredentialsProvider cp = new BasicCredentialsProvider();
//        cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//        return cp;
//    }
//}
