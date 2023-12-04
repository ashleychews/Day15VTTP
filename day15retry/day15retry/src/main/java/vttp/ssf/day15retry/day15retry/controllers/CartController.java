package vttp.ssf.day15retry.day15retry.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.day15retry.day15retry.Utils;
import vttp.ssf.day15retry.day15retry.models.Item;
import vttp.ssf.day15retry.day15retry.services.CartService;


@Controller
@RequestMapping(path="/cart")
public class CartController {

    private Logger logger = Logger.getLogger(CartController.class.getName());

    //defining item,cart,username
    public static final String ATTR_ITEM = "item";
    public static final String ATTR_CART = "cart";
    public static final String ATTR_USERNAME = "username";

    @Autowired
    private CartService cartSvc;
    
    @GetMapping
    public String getCart(@RequestParam String username, Model model, HttpSession sess){
        
        //get the cart for the particular user
        List<Item> cart =  cartSvc.getCart(ATTR_USERNAME);
        
        logger.info("CART: %s - %s\n".formatted(username, cart));

        // add cart object to the session, cart remain in the session until session is destroyed
        sess.setAttribute(Utils.ATTR_CART, cart);

        model.addAttribute(ATTR_ITEM, new Item());
        model.addAttribute(ATTR_CART, cart);
        model.addAttribute(ATTR_USERNAME, username);
        
        return "cart";
    }

    //post mapping for destroying session
    @PostMapping(path = "/checkout")
    public String postCartCheckout(HttpSession sess, @RequestParam String username) {
        
        //get cart for current session
        List<Item> cart = Utils.getCart(sess);
        System.out.printf("Checking out cart: %s\n", cart);

        //save the cart in redis
        cartSvc.save(username, cart);

        //invalidate current session
        sess.invalidate();
        
        return "redirect:/"; //return back to index page

    }

    //post mapping for validating form fields
    @PostMapping
    public ModelAndView postCart(@Valid @ModelAttribute(ATTR_ITEM) Item item, 
        BindingResult bindings, HttpSession sess,
        @RequestParam String username, @RequestBody String body) {
        
        ModelAndView mav = new ModelAndView("cart");

        //if there are validation errors, return to cart.html and report errors
        if (bindings.hasErrors()) {
            mav.setStatus(HttpStatusCode.valueOf(400)); //error
            return mav;
        }
        
        //get cart of current session
        List<Item> cart = Utils.getCart(sess);
        //add the item from the form to cart
        cart.add(item);

        mav.addObject(ATTR_ITEM, new Item());
        mav.addObject(ATTR_CART, cart);
        mav.addObject(ATTR_USERNAME, username);

        mav.setStatus(HttpStatusCode.valueOf(200)); //status OK

        return mav;
        }

}
