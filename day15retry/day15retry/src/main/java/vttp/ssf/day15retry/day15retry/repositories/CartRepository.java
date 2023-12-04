package vttp.ssf.day15retry.day15retry.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.day15retry.day15retry.models.Item;

//PERSISTANCE

@Repository
public class CartRepository {
    
    //connect to redis
    @Autowired @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    public boolean hasCart(String username) {
        return template.hasKey(username);
    }

    public void deleteCart(String username) {
        template.delete(username);
    }

    public void addCart(String username, List<Item> cart) {
        ListOperations<String, String> list = template.opsForList();
        cart.stream()
        .forEach(item -> list.leftPush(username, "%s,%d".formatted(item.getName(), item.getQuantity())));
    }
    
    //get cart of user
    public List<Item> getCart(String username) {
        ListOperations<String, String> list = template.opsForList();
        List<Item> cart  = new LinkedList<>();
        Long size = list.size(username);
        for (String i : list.range(username, 0, size)) {
            //split into name of item and quantity
            String[] terms = i.split(",");
            //create new object
            Item item = new Item();
            item.setName(terms[0]);
            item.setQuantity(Integer.parseInt(terms[1]));
            cart.add(item);

        }
        return cart;
    }



}
