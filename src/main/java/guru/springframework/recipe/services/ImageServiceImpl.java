package guru.springframework.recipe.services;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {

        try {

            Recipe recipe = recipeRepository.findById(recipeId).get();

            byte[] bytes = file.getBytes();
            Byte[] byteObjects = new Byte[bytes.length];
            Arrays.setAll(byteObjects, n -> bytes[n]);

            recipe.setImage(byteObjects);

            recipeRepository.save(recipe);

        } catch (IOException e) {
            // todo handle better
            log.error("Error occured when saving the image", e);

            e.printStackTrace();
        }
    }
}
