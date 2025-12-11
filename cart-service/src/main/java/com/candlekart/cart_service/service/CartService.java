package com.candlekart.cart_service.service;

import com.candlekart.cart_service.dto.CartItemDto;
import com.candlekart.cart_service.dto.CartItemRequest;
import com.candlekart.cart_service.dto.CartResponse;
import com.candlekart.cart_service.dto.OrderResponse;
import com.candlekart.cart_service.exc.NotFoundException;
import com.candlekart.cart_service.feign.FeignOrderClient;
import com.candlekart.cart_service.kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Optional;

import static com.candlekart.cart_service.constants.Constants.CART_PREFIX;

@Slf4j
@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private FeignOrderClient feignOrderClient;

//    Todo
//    So your cart architecture will be:
//    Redis stores the cart for 24 hours (fast access)
//    Database keeps the cart forever (or until user clears / order placed)
//    When Redis expires the cart, you rebuild it from DB the next time user opens app.

    public CartResponse getCart(String userId) {
        return getCartFromRedis(userId);
    }

    public CartResponse addItem(String userId, CartItemRequest request) {
        CartResponse cart = getCartFromRedis(userId);

        Optional<CartItemDto> existing = cart.getItems().stream()
                .filter(i -> i.getSku().equals(request.getSku()))
                .findFirst();

        if(existing.isPresent()){
            existing.get().setQuantity(request.getQty());
        }else{
            cart.getItems().add(CartItemDto.builder()
                    .sku(request.getSku())
                    .quantity(request.getQty())
                    .build());
        }

        saveCartToRedis(userId, cart);
        return cart;
    }

    public CartResponse removeItem(String userId, String sku) {
        CartResponse cart = getCartFromRedis(userId);

        cart.getItems().removeIf(i -> i.getSku().equals(sku));
        saveCartToRedis(userId, cart);
        return cart;
    }

    public void clearCart(String userId) {
        redisTemplate.delete(CART_PREFIX + userId);
    }

    public void checkout(String userId) throws JsonProcessingException {
        try{
            CartResponse cartResponse = getCart(userId);
            if(cartResponse.getItems().isEmpty())
                throw new NotFoundException("User not found or Cart is Empty");

            //Calling order create method to create method
            ResponseEntity<OrderResponse> orderResponse = feignOrderClient.createOrder(cartResponse);
            if(orderResponse.getStatusCode() == HttpStatus.CREATED || orderResponse.getStatusCode() == HttpStatus.OK){
                clearCart(userId);
            }else{
                throw new RuntimeException("Unable to Create Order");
            }
        }catch (FeignException e) {
            throw new RuntimeException(getErrorMessage(e));
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }


    private CartResponse getCartFromRedis(String userId){
        Object obj = redisTemplate.opsForValue().get(CART_PREFIX + userId);
        if(obj != null){
            return (CartResponse) obj;
        }

        return CartResponse.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .build();
    }
    private void saveCartToRedis(String userId, CartResponse cart) {
        redisTemplate.opsForValue().set(CART_PREFIX + userId, cart);
    }








    private void publish(String topic, Object evt) {
        kafkaProducer.publish(topic, evt);
    }
    private String getErrorMessage(FeignException e) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(e.contentUTF8());
        return json.get("error").asText();
    }


}
