package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class RecipeAbstractRepositoryTest {

    private MockWebServer server;
    private RecipeAbstractRepository repository;
    // Use this rule to instantly execute any background operation on the calling thread.
    @Rule // Stellt sicher, dass für LiveDate der richtige Thread verwendet wird
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();

    @Mock
    private Observer<Resource<List<RecipeAbstractEntity>>> observer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        server = new MockWebServer();
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("[{\"title\":\"Apfel-Mascarpone-Creme mit Amarettini\",\"id\":\"4ab99cc8-b21a-4146-97ef-a7949184a173\",\"editingDate\":1487459207052}," +
                        "{\"title\":\"Arabische Pasta\",\"id\":\"c0e5582e-252f-4e94-8a49-e12b4b047afb\",\"editingDate\":1515082740753}]"));

        try {
            server.start();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        final HttpUrl baseUrl = server.url("/");
        final Context context = InstrumentationRegistry.getTargetContext();
        final ServiceLocator serviceLocator = new ServiceLocatorForTest(context, baseUrl.toString());
        repository = new RecipeAbstractRepository(serviceLocator.getRecipeService(), serviceLocator.getRecipeAbstractDao());

    }

    private RecipeAbstractEntity createRecipeAbstractEntity() {
        RecipeAbstractEntity r = new RecipeAbstractEntity();
        r.setRecipeId("4135431253");
        r.setLastUpdate(243123431L);
        r.setTitle("all 4 one");
        return r;
    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldBeRecipeCollection() throws InterruptedException {

      // when
        final Resource<List<RecipeAbstractEntity>> recipesResource =
                LiveDataTestUtil.getValue(repository.getRecipes(), 2);

        // then
        assertThat(recipesResource, is(notNullValue()));
        assertThat(recipesResource.data, is(notNullValue()));
        assertThat(recipesResource.data.size(), is(2));

        final Resource<List<RecipeAbstractEntity>> recipesResource1 =
                LiveDataTestUtil.getValue(repository.getRecipes(), 2);

        // then
        assertThat(recipesResource1, is(notNullValue()));
        assertThat(recipesResource1.data, is(notNullValue()));
        assertThat(recipesResource1.data.size(), is(2));
    }

    @Test
    public void shouldBeRecipeCollectionObserver() throws InterruptedException {

        // when
        repository.getRecipes().observeForever(observer);

        Thread.sleep(200);

        // then
        verify(observer, times(2)).onChanged(any());
    }

    @Test
    public void shouldBeRecipeCollectionFromRemoteDataService() throws IOException {

        // when
        final List<RecipeAbstractEntity> recipes = repository.getRecipeCollectionFromRemote();

        // then
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.size(), is(2));

    }

    @Test
    public void shouldBeUpdateDatabase() {
        // given
        final List<RecipeAbstractEntity> recipes = new ArrayList<>(1);
        recipes.add(createRecipeAbstractEntity());

        // when
        repository.updateAllDataInDatabase(recipes);

        // then
        final List<RecipeAbstractEntity> recipesFromDb = repository.getRecipeCollectionFromDatabase();
        assertThat(recipesFromDb, is(notNullValue()));
        assertThat(recipes.size(), is(1));
    }

}
