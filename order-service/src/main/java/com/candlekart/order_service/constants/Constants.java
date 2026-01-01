package com.candlekart.order_service.constants;

public class Constants {
    public static final String CART_CHECKOUT_TOPIC = "create-checkout";
    public static final String Create_Order_To_Inventory_Topic_Name = "order.inventory.created";
    public static final String Payment_Order_To_Payment_Topic_Name = "cart.order.payment";
    public static final String Payment_Payment_To_Order_Topic_Name = "cart.payment.order";
    public static final String Reserve_Order_To_Inventory_Topic_Name = "order.inventory.reserve";
    public static final String Release_Stock_To_Inventory_Topic_Name = "order.inventory.release";
}
