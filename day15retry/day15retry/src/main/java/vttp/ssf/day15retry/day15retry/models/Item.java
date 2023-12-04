package vttp.ssf.day15retry.day15retry.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Item {

    //name of user
    @NotEmpty(message = "Please enter your name")
    private String username;

    //name of object
    @NotEmpty(message = "Please enter name of item")
    private String name;

    //item quantity
    @NotNull(message = "Quantity cannot be null")
    @Min(value=1, message = "Must have at least 1")
    @Max(value=20, message = "Cannot exceed 20")
    private Integer quantity;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
       return "Item [name=" + name + ", quantity=" + quantity + "]";
    }
}
