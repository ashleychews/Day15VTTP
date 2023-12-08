package vttp.ssf.day15workshop.day15workshop.model;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


//properties=name,email,phone no and DOB
public class Contact {

    @NotEmpty(message = "Name is mandatory")
    @Size(min=3, max=64, message="Length of Name must be between 3 and 64")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "(8|9)[0-9]{7}", message = "invalid phone number entered")
    //can start with 8/9, following 7 digits can be between 0-9
    private String phoneNo;

    @NotNull(message = "Date of Birth is mandatory") //@NotEmpty constraint is typically used for validating strings, collections, or arrays
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Birth date must be a past date less than today")
    private LocalDate birthDay;

    @Min(value = 10, message = "Must be above 10 years old")
    @Max(value = 100, message = "Must be below 100 years old")
    private Integer age;

    private String id;
    
    public void calculateAge() {
        int calculatedAge = 0;
        if ((birthDay != null)) {
            calculatedAge = Period.between(birthDay, LocalDate.now()).getYears();
        }
        setAge(calculatedAge);
    }

    //toString method affects the readability of the string representation
    @Override
    public String toString() {
        return "Contact{" +
                "\nname='" + name + '\'' +
                "\nemail='" + email + '\'' +
                "\nphoneNo='" + phoneNo + '\'' +
                "\nbirthDay=" + birthDay +
                "\nage=" + age +
                "\nid='" + id + '\'' +
                "\n}";
    }

    //Contact{
    //name='John Doe'
    //email='john@example.com'
    //phoneNo='123456789'
    //birthDay=1990-01-01
    //age=32
    //id='abc123'
    //}
    
}
