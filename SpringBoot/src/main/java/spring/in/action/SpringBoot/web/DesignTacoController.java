package spring.in.action.SpringBoot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import spring.in.action.SpringBoot.Ingredient;
import spring.in.action.SpringBoot.Order;
import spring.in.action.SpringBoot.Taco;
import spring.in.action.SpringBoot.data.IngredientRepository;
import spring.in.action.SpringBoot.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static spring.in.action.SpringBoot.Ingredient.Type;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes({"order", "design"})
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository designRepo;


    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @GetMapping
    public String showDesignForm(Model model) {
        addIngredientListToModel(model);

        return "design";
    }

    private void addIngredientListToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll()
                      .forEach(ingredients::add);

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                          .filter(i -> i.getType().equals(type))
                          .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "design")
    public Taco taco() {
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute(name = "design") Taco design, Errors errors, @ModelAttribute Order order, Model model) {
        if (errors.hasErrors()) {
            addIngredientListToModel(model);
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        model.addAttribute("design", new Taco());

        return "redirect:/orders/current";
    }
}
