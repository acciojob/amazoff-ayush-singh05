package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
      Map<String,Order> orderdb = new HashMap<>();
      Map<String,DeliveryPartner> deliverydb = new HashMap<>();
      Map<String ,String> partnerdb = new HashMap<>();
      Map<String, List<String>> dpDB = new HashMap<>();

      public void addOrder(Order order) {
            String key = order.getId();

            orderdb.put(key,order);

      }

      public void addPartner(String partnerId) {
            DeliveryPartner dp = new DeliveryPartner(partnerId);

            deliverydb.put(partnerId,dp);
      }

      public void addOrderPartnerPair(String orderId, String partnerId) {
            if(orderdb.containsKey(orderId) && deliverydb.containsKey(partnerId)) {
                  partnerdb.put(orderId, partnerId);
            }
            List<String> ol = new ArrayList<>();

            if(dpDB.containsKey(partnerId)){
                  ol = dpDB.get(partnerId);
            }
            ol.add(orderId);
            dpDB.put(partnerId,ol);
            DeliveryPartner dp = deliverydb.get(partnerId);
            dp.setNumberOfOrders(ol.size());

      }

      public Order getOrderByID(String orderId) {


                  return orderdb.get(orderId);

      }

      public DeliveryPartner getPartnerById(String partnerId) {

                  return deliverydb.get(partnerId);

      }

      public Integer getOrderCountByPartnerId(String partnerId) {
           return dpDB.get(partnerId).size();
      }

      public List<String> getOrdersByPartnerId(String partnerId) {
            return dpDB.get(partnerId);
      }

      public List<String> getAllorders() {
            List<String>allOrder = new ArrayList<>();
            for (String order : orderdb.keySet()){
                  allOrder.add(order);
            }
            return allOrder;
      }

      public Integer getCountOfUnassignedOrders() {
            return orderdb.size()- partnerdb.size();
      }

      public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
            int cnt = 0;
            List<String> orders = dpDB.get(partnerId);

            for(String orderId: orders){
                  int deliveryTime = orderdb.get(orderId).getDeliveryTime();
                  if(deliveryTime>time)
                        cnt++;
            }
            return cnt;
      }

      public int getLastDeliveryTimeByPartnerId(String partnerId) {
            int maxTime = 0;
            List<String> orders = dpDB.get(partnerId);
            for(String orderId: orders){
                  int currentTime = orderdb.get(orderId).getDeliveryTime();
                  maxTime = Math.max(maxTime,currentTime);
            }

            return maxTime;
      }

      public void deletePartnerById(String partnerId) {
            deliverydb.remove(partnerId);

            List<String> listOfOrders = dpDB.get(partnerId);
            dpDB.remove(partnerId);

            for(String order: listOfOrders){
                  partnerdb.remove(order);
            }
      }

      public void deleteOrderById(String orderId) {
            orderdb.remove(orderId);

            String partnerId = partnerdb.get(orderId);
            partnerdb.remove(orderId);

            dpDB.get(partnerId).remove(orderId);

            deliverydb.get(partnerId).setNumberOfOrders(dpDB.get(partnerId).size());

      }
}
