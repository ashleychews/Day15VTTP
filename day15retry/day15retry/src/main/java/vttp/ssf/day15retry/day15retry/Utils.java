package vttp.ssf.day15retry.day15retry;

import java.util.LinkedList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.day15retry.day15retry.models.Item;

public class Utils {

    public static final String ATTR_CART = "cart";

    public static List<Item> getCart(HttpSession sess) {
        //get cart from session
        List<Item> cart = (List<Item>)sess.getAttribute(ATTR_CART); 
        //if cart is empty, create a new cart
        if (null == cart) {
            cart = new LinkedList<>();
            sess.setAttribute(ATTR_CART, cart);
        }
        return cart;
    }
    
}
