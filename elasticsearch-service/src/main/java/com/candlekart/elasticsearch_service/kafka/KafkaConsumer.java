package com.candlekart.elasticsearch_service.kafka;

import com.candlekart.elasticsearch_service.dto.ElasticSearchProductList;
import com.candlekart.elasticsearch_service.exc.BadRequestException;
import com.candlekart.elasticsearch_service.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.candlekart.elasticsearch_service.constants.Constants.Create_Product_In_ElasticSearch_Topic_Name;
import static com.candlekart.elasticsearch_service.constants.Constants.Update_Product_In_ElasticSearch_Topic_Name;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private ElasticSearchService service;

    @KafkaListener(topics = Create_Product_In_ElasticSearch_Topic_Name, groupId = "elasticsearch-service")
    public void onProductCreate(ElasticSearchProductList productList) {
        log.info("ElasticSearch Service : onProductCreate : Init");
        if (productList == null || productList.getProductsList() == null)
            throw new BadRequestException("Kafka message is null or invalid");
        log.info("ElasticSearch Service : onProductCreate : End");
        service.addAllProducts(productList);
    }
    @KafkaListener(topics = Update_Product_In_ElasticSearch_Topic_Name, groupId = "elasticsearch-service")
    public void onProductUpdate(ElasticSearchProductList productList) {
        if (productList == null || productList.getProductsList() == null)
            throw new BadRequestException("Kafka message is null or invalid");
        service.updateAllProducts(productList.getProductsList().get(0));
    }


}
