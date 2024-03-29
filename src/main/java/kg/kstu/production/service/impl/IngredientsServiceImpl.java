package kg.kstu.production.service.impl;

import kg.kstu.production.entity.Ingredient;
import kg.kstu.production.repository.IngredientsRepository;
import kg.kstu.production.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientsServiceImpl implements IngredientsService {
    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Override
    public List<Ingredient> getAll(Long productId) {
        return ingredientsRepository.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), productId))
                .sorted(Comparator.comparing(i -> i.getMaterial().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String createIngredient(Ingredient ingredient) {
        boolean contains = ingredientsRepository.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), ingredient.getProduct().getId()))
                .anyMatch(i -> i.getMaterial().getId().equals(ingredient.getMaterial().getId()));
        if(contains) {
            return "Ingredient already contains";
        } else {
            ingredientsRepository.save(ingredient);
            return "Ingredient added";
        }
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientsRepository.deleteById(id);
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return ingredientsRepository.findById(id);
    }

    @Override
    public String updateIngredient(Ingredient ingredient) {
        boolean contains = ingredientsRepository.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), ingredient.getProduct().getId()))
                .anyMatch(i -> Objects.equals(i.getMaterial().getId(), ingredient.getMaterial().getId()));
        if (!contains) {
            ingredientsRepository.save(ingredient);
            return "The product added";
        } else {
            Optional<Ingredient> ingredientToUpdateOptional = ingredientsRepository.findById(ingredient.getId());
            if (ingredientToUpdateOptional.isEmpty()) {
                return "Ingredient not found";
            } else {
                Ingredient ingredientToUpdate = ingredientToUpdateOptional.get();
                ingredientToUpdate.setQuantity(ingredient.getQuantity());
                ingredientsRepository.save(ingredientToUpdate);
                return "The product already contains";
            }
        }
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientsRepository.save(ingredient);
    }

    /*private IngredientsDto mapToIngredientsDto(Ingredients ingredient) {
        return IngredientsDto.builder()
                .id(ingredient.getId())
                .product((ingredient.getProduct()))
                .material(ingredient.getMaterial())
                .quantity(ingredient.getQuantity())
                .build();
    }*/
}
