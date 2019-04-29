package spring.in.action.SpringBoot;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Street is required")
    private String street;
    @NotBlank(message = "city is required")
    private String city;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "Zip is required")
    @Size(min = 5, max = 5)
    @Digits(integer = 5, fraction = 0, message = "Zip must be 5 digits")
    private String zip;
    @NotBlank
    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be of format MM/YY")
    private String ccExpiration;
    @NotBlank
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;
    private Long id;
    private Date placedAt;
    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }
}
