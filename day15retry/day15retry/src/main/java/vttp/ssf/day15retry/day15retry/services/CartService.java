package vttp.ssf.day15retry.day15retry.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.ssf.day15retry.day15retry.models.Item;
import vttp.ssf.day15retry.day15retry.repositories.CartRepository;

//BUSINESS LOGIC

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepo;

    //getting cart if it exist, otherwise return empty cart
    public List<Item> getCart(String username) {
        if (cartRepo.hasCart(username))
            //return cart
            return cartRepo.getCart(username);
            //otherwise return empty cart
        return new LinkedList<>();
    }   

    //save to redis
    public void save(String username, List<Item> cart) {
        cartRepo.deleteCart(username); //deletes exisitng cart
        cartRepo.addCart(username, cart); //replace with new cart
    }


}
