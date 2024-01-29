package org.idk.tacocloud.dao;

import org.idk.tacocloud.entity.Ingredient;

import java.util.Iterator;
import java.util.Optional;

public interface IngredientRepository {
    Iterator<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
