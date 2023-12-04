package vttp.ssf.day15.day15.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.day15.day15.Utils;
import vttp.ssf.day15.day15.models.Item;

@Repository
public class CartRepository {

   //connect to reddis
   @Autowired @Qualifier(Utils.BEAN_REDIS)
   private RedisTemplate<String, String> template;

   public boolean hasCart(String name) {
      return template.hasKey(name);
   }

   public void deleteCart(String name) {
      template.delete(name);
   }

   public void addCart(String name, List<Item> cart) {
      ListOperations<String, String> list = template.opsForList();
      cart.stream()
         .forEach(item -> {
            // first column is name, second is quantity
            list.leftPush(name, "%s,%d".formatted(item.getName(), item.getQuantity()));
         });
   }

   public List<Item> getCart(String name) {
      ListOperations<String, String> list = template.opsForList();
      Long size = list.size(name);
      List<Item> cart = new LinkedList<>();
      for (String i: list.range(name, 0, size)) {
         String[] terms = i.split(","); //rmb its like a csv file -> split to name,quantity
         Item item = new Item(); //create a new item object to store the parsed value
         item.setName(terms[Utils.F_NAME]); //set name
         item.setQuantity(Integer.parseInt(terms[Utils.F_QUANTITY])); //set quantity
         cart.add (item); //add item to cart
      }
      return cart; 

   }
   

   
}
