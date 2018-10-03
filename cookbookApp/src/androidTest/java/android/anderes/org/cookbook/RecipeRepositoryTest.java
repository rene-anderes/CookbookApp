package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.repository.RecipeRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

public class RecipeRepositoryTest {

    private RecipeRepository repository;
    // Use this rule to instantly execute any background operation on the calling thread.
    @Rule // Stellt sicher, dass für LiveDate der richtige Thread verwendet wird
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{\"id\":\"c0e5582e-252f-4e94-8a49-e12b4b047afb\",\"noOfPeople\":\"2\",\"ingredients\":[{\"description\":\"Curry\",\"portion\":null,\"comment\":null},{\"description\":\"getrocknete Feigen oder Datteln\",\"portion\":\"4-8 Stk.\",\"comment\":\"inStreifen\"},{\"description\":\"Zwiebel\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Kokosnussmilch\",\"portion\":\"250 mL\",\"comment\":null},{\"description\":\"Penne\",\"portion\":\"250\",\"comment\":null},{\"description\":\"Tofu\",\"portion\":\"250 g\",\"comment\":null},{\"description\":\"Knoblizehen\",\"portion\":\"2\",\"comment\":null},{\"description\":\"Salz, Pfeffer und Rapsöl\",\"portion\":null,\"comment\":null},{\"description\":\"Kreuzkümmel\",\"portion\":null,\"comment\":\"frisch gemörsert\"},{\"description\":\"Peperoncini\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Korianderblätter\",\"portion\":\"½ Bund\",\"comment\":null}],\"tags\":[\"pasta\",\"fleischlos\"],\"title\":\"Arabische Pasta\",\"preparation\":\"<p>Tofu in Würfel schneiden und mit etwas Öl anbraten. Kurz befor der Tofu aus der Pfanne genommen wird, mit Curry und Kreuzkümmel bestreuen und den Tofu kurz duchschwenken. Danach aus der Pfanne nehmen.<br />\\n<br />\\nWasser aufsetzen, salzen und Pasta al dente kochen.<br />\\n<br />\\nIn der Zwischenzeit die in Streifen geschnittenen Zwiebel kurz anbraten, anschlissend den in Streifen geschnittenen Peperoncini dazu geben. Etwas später die Knoblizehen (in Würfelchen) in die Pfanne geben und mit Salz würzen. Anschliessend die Kokosnussmilch dazu geben, kurz aufkochen lassen.&nbsp;Dann Temperatur zurücknehmen. Zu langes Kochen tut der Kokosnussmilch nicht gut. Ca. 4 Minuten bevor die Pasta al dente sind den Tofu in die Sauce geben und nur noch warm werden lassen. Kurz bevor die Spaghetti mit der Sauce vermischt wird, die getrockneten Feigen (in Streifen geschnitten) in die Sauce geben.</p>\\n\\n<p>Die Sauce und die Spaghetti nach dem mischen zugedeckt ein paar Minuten ziehen lassen. Zum Servieren noch etwas frisch gemahlener Pfeffer und ein paar Korianderblätter über die Pasta geben.<br />\\n&nbsp;</p>\\n\",\"preamble\":\"<p>Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (getrocknete Feigen oder Datteln) gemischt wird, habe ich diese Rezept \\\"Arabische Pasta\\\" benannt.</p>\\n\",\"rating\":5,\"addingDate\":1250959818424,\"editingDate\":1515082740753,\"image\":null}"));
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{\"id\":\"c0e5582e-252f-4e94-8a49-e12b4b047afc\",\"noOfPeople\":\"2\",\"ingredients\":[{\"description\":\"Curry\",\"portion\":null,\"comment\":null},{\"description\":\"getrocknete Feigen oder Datteln\",\"portion\":\"4-8 Stk.\",\"comment\":\"inStreifen\"},{\"description\":\"Zwiebel\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Kokosnussmilch\",\"portion\":\"250 mL\",\"comment\":null},{\"description\":\"Penne\",\"portion\":\"250\",\"comment\":null},{\"description\":\"Tofu\",\"portion\":\"250 g\",\"comment\":null},{\"description\":\"Knoblizehen\",\"portion\":\"2\",\"comment\":null},{\"description\":\"Salz, Pfeffer und Rapsöl\",\"portion\":null,\"comment\":null},{\"description\":\"Kreuzkümmel\",\"portion\":null,\"comment\":\"frisch gemörsert\"},{\"description\":\"Peperoncini\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Korianderblätter\",\"portion\":\"½ Bund\",\"comment\":null}],\"tags\":[\"pasta\",\"fleischlos\"],\"title\":\"Arabische Pasta\",\"preparation\":\"<p>Tofu in Würfel schneiden und mit etwas Öl anbraten. Kurz befor der Tofu aus der Pfanne genommen wird, mit Curry und Kreuzkümmel bestreuen und den Tofu kurz duchschwenken. Danach aus der Pfanne nehmen.<br />\\n<br />\\nWasser aufsetzen, salzen und Pasta al dente kochen.<br />\\n<br />\\nIn der Zwischenzeit die in Streifen geschnittenen Zwiebel kurz anbraten, anschlissend den in Streifen geschnittenen Peperoncini dazu geben. Etwas später die Knoblizehen (in Würfelchen) in die Pfanne geben und mit Salz würzen. Anschliessend die Kokosnussmilch dazu geben, kurz aufkochen lassen.&nbsp;Dann Temperatur zurücknehmen. Zu langes Kochen tut der Kokosnussmilch nicht gut. Ca. 4 Minuten bevor die Pasta al dente sind den Tofu in die Sauce geben und nur noch warm werden lassen. Kurz bevor die Spaghetti mit der Sauce vermischt wird, die getrockneten Feigen (in Streifen geschnitten) in die Sauce geben.</p>\\n\\n<p>Die Sauce und die Spaghetti nach dem mischen zugedeckt ein paar Minuten ziehen lassen. Zum Servieren noch etwas frisch gemahlener Pfeffer und ein paar Korianderblätter über die Pasta geben.<br />\\n&nbsp;</p>\\n\",\"preamble\":\"<p>Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (getrocknete Feigen oder Datteln) gemischt wird, habe ich diese Rezept \\\"Arabische Pasta\\\" benannt.</p>\\n\",\"rating\":5,\"addingDate\":1250959818424,\"editingDate\":1515082740753,\"image\":null}"));

        try {
            server.start();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        final HttpUrl baseUrl = server.url("/");
        final Context context = InstrumentationRegistry.getTargetContext();
        final ServiceLocator serviceLocator = new ServiceLocatorForTest(context, baseUrl.toString());
        repository = new RecipeRepository(serviceLocator.getRecipeService(), serviceLocator.getRecipeDao());
    }

    @Test
    public void shouldBeCorrectRecipe() throws Exception {

        // when
        final Resource<RecipeEntity> recipeResource =
                LiveDataTestUtil.getValue(repository.getRecipe("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);

        // then
        assertThat(recipeResource, is(notNullValue()));
        assertThat(recipeResource.status, is(Resource.Status.SUCCESS));
        assertThat(recipeResource.data, is(notNullValue()));
        assertThat(recipeResource.data.getRecipeId(), is("c0e5582e-252f-4e94-8a49-e12b4b047afb"));
        assertThat(recipeResource.data.getTitle(), is("Arabische Pasta"));
        assertThat(recipeResource.data.getAddingDate(), is(1250959818424L));
        assertThat(recipeResource.data.getEditingDate(), is(1515082740753L));
        assertThat(recipeResource.data.getNoOfPeople(), is("2"));
        assertThat(recipeResource.data.getPreamble(), is(notNullValue()));
        assertThat(recipeResource.data.getPreparation(), is(notNullValue()));
        assertThat(recipeResource.data.getRating(), is(5));
        assertThat(recipeResource.data.getTags(), is(notNullValue()));
        assertThat(recipeResource.data.getTags().size(), is(2));
    }

    @Test
    public void checkIsSyncNecessary() throws Exception {

        // given
        LiveDataTestUtil.getValue(repository.getRecipe("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);
        LiveDataTestUtil.getValue(repository.getRecipe("c0e5582e-252f-4e94-8a49-e12b4b047afc"), 2);

        // when
        boolean isNecessary = repository.isSyncNecessary("c0e5582e-252f-4e94-8a49-e12b4b047afb", 1515082740753L);
        // then
        assertThat(isNecessary, is(false));

        // when
        isNecessary = repository.isSyncNecessary("c0e5582e-252f-4e94-8a49-e12b4b047afb", 1515082740754L);
        // then
        assertThat(isNecessary, is(true));

        // when
        isNecessary = repository.isSyncNecessary("not exits", 1515082740753L);
        // then
        assertThat(isNecessary, is(true));

    }


    @Test
    public void shouldBeRecipeCollectionFromRemoteDataService() throws IOException {

        // when
        final RecipeEntity recipe = repository.getRecipeFromRemote("c0e5582e-252f-4e94-8a49-e12b4b047afb");

        // then
        assertThat(recipe, is(notNullValue()));
        assertThat(recipe.getRecipeId(), is("c0e5582e-252f-4e94-8a49-e12b4b047afb"));
        assertThat(recipe.getTitle(), is("Arabische Pasta"));
        assertThat(recipe.getAddingDate(), is(1250959818424L));
        assertThat(recipe.getEditingDate(), is(1515082740753L));
        assertThat(recipe.getNoOfPeople(), is("2"));
        assertThat(recipe.getPreamble(), is(notNullValue()));
        assertThat(recipe.getPreparation(), is(notNullValue()));
        assertThat(recipe.getRating(), is(5));
        assertThat(recipe.getTags(), is(notNullValue()));
        assertThat(recipe.getTags().size(), is(2));

    }

    @Test
    public void shouldBeNoDataFromDatabase() {

        //when
        final RecipeEntity recipe = repository.getRecipeFromDatabase("c0e5582e-252f-4e94-8a49-e12b4b047afb");

        // then
        assertThat(recipe, is(nullValue()));
    }

    @Test
    public void shuoldBeCorrectSaveNewData() {

        // when
        repository.saveDataToDatabase(createEntity());

        // then
        final RecipeEntity recipe = repository.getRecipeFromDatabase("987654321");
        assertThat(recipe, is(notNullValue()));
        assertThat(recipe.getRecipeId(), is("987654321"));
        assertThat(recipe.getTitle(), is("all 4 one"));
        assertThat(recipe.getAddingDate(), is(1250959818424L));
        assertThat(recipe.getEditingDate(), is(1515082740753L));
        assertThat(recipe.getNoOfPeople(), is("2"));
        assertThat(recipe.getPreamble(), is(nullValue()));
        assertThat(recipe.getPreparation(), is("... ist ganz einfach ..."));
    }

    @Test
    public void shuoldBeCorrectSaveExistsData() {

        // given
        repository.saveDataToDatabase(createEntity());
        final RecipeEntity recipe = repository.getRecipeFromDatabase("987654321");
        assert recipe != null;

        // when
        recipe.setPreparation("... ist noch viel einfacher ...");
        repository.saveDataToDatabase(recipe);

        // then
        final RecipeEntity updatedRecipe = repository.getRecipeFromDatabase("987654321");

        assertThat(updatedRecipe, is(notNullValue()));
        assertThat(updatedRecipe.getRecipeId(), is("987654321"));
        assertThat(updatedRecipe.getTitle(), is("all 4 one"));
        assertThat(updatedRecipe.getAddingDate(), is(1250959818424L));
        assertThat(updatedRecipe.getEditingDate(), is(1515082740753L));
        assertThat(updatedRecipe.getNoOfPeople(), is("2"));
        assertThat(updatedRecipe.getPreamble(), is(nullValue()));
        assertThat(updatedRecipe.getPreparation(), is("... ist noch viel einfacher ..."));
    }

    @Test
    public void shouldBeRemoveOrphan() {
        // given
        repository.saveDataToDatabase(createEntity());
        final List<String> recipeIds = Arrays.asList("123445678", "759347569");

        // when
        repository.deleteOrphan(recipeIds);

        // then
        assertThat(repository.getRecipeFromDatabase("987654321"), is(nullValue()));
    }

    @Test
    public void shouldBeNotRemoveOrphan() {
        // given
        repository.saveDataToDatabase(createEntity());
        final List<String> recipeIds = Arrays.asList("987654321", "759347569");

        // when
        repository.deleteOrphan(recipeIds);

        // then
        assertThat(repository.getRecipeFromDatabase("987654321"), is(notNullValue()));
    }

    private RecipeEntity createEntity() {
        final RecipeEntity entity = new RecipeEntity();
        entity.setRecipeId("987654321");
        entity.setTitle("all 4 one");
        entity.setNoOfPeople("2");
        entity.setPreparation("... ist ganz einfach ...");
        entity.setAddingDate(1250959818424L);
        entity.setEditingDate(1515082740753L);
        return entity;
    }
}
