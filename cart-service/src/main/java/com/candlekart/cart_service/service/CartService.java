package com.candlekart.cart_service.service;

import com.candlekart.cart_service.dto.CartItemDto;
import com.candlekart.cart_service.dto.CartItemRequest;
import com.candlekart.cart_service.dto.CartResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    public void checkout(String userId) {
        // TODO: Produce Kafka Event `cart.checkedout`
        // Order Service will take over
        clearCart(userId);
    }


    private CartResponse getCartFromRedis(String userId){
        Object obj = redisTemplate.opsForValue().get(CART_PREFIX + userId);
        if(obj != null){
            log.info("CartService : "+ obj + " " + obj.getClass());
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



}
