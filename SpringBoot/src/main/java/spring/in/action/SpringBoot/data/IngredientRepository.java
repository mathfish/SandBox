package spring.in.action.SpringBoot.data;

import spring.in.action.SpringBoot.Ingredient;

public interface IngredientRepository {

    Iterable<Ingredient> findAll();

    Ingredient findOne(String id);

    Ingredient save(Ingredient ingredient);
}
