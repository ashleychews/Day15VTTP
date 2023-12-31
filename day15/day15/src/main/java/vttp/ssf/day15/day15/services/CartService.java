package vttp.ssf.day15.day15.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.ssf.day15.day15.models.Item;
import vttp.ssf.day15.day15.repositories.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    //getting cart if it exist, otherwise return empty cart
    public List<Item> getCart(String name) {
        if (cartRepo.hasCart(name))
            //return cart
            return cartRepo.getCart(name);
            //otherwise return empty cart
        return new LinkedList<>();
    }   

    //save to redis
    public void save(String name, List<Item> cart) {
        cartRepo.deleteCart(name); //deletes exisitng cart
        cartRepo.addCart(name, cart); //replace with new cart
    }

}
