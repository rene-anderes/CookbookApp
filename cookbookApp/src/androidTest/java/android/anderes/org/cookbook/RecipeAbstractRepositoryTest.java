package android.anderes.org.cookbook;

import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.model.Resource;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
public class RecipeAbstractRepositoryTest {

    @Test
    public void shouldBeRecipeCollection() throws InterruptedException {
        // given
        final RecipeAbstractRepository repository = new RecipeAbstractRepository();
        // when
        final Resource<List<RecipeAbstract>> recipesResource = LiveDataTestUtil.getValue(repository.getRecipes());
        // then
        assertThat(recipesResource, is(notNullValue()));
        assertThat(recipesResource.data, is(notNullValue()));
    }
}
