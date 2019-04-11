package spring.in.action.SpringBoot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.in.action.SpringBoot.Ingredient;
import spring.in.action.SpringBoot.Taco;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static spring.in.action.SpringBoot.Ingredient.*;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients
                = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                                new Ingredient("JACK", "Monterrey", Type.CHEESE),
                                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                                new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("design", new Taco());

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                          .filter(i -> i.getType().equals(type))
                          .collect(Collectors.toList());
    }

    @PostMapping
    public String processDesign(Taco design) {
        log.info("Process design " + design);
        return "redirect:/orders/current";
    }
}