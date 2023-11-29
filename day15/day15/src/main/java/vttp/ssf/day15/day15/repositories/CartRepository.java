package vttp.ssf.day15.day15.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

   // Get cart contents
   public String getCartContents(String name) {
      return template.opsForValue().get(name);
   }

   // Create a new cart
   public void createCart(String name, String initialContents) {
      template.opsForValue().set(name, initialContents);
   }
   
}
