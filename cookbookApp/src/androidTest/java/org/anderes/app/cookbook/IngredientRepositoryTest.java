package org.anderes.app.cookbook;

import org.anderes.app.cookbook.database.IngredientEntity;
import org.anderes.app.cookbook.repository.IngredientRepository;
import org.anderes.app.cookbook.repository.Resource;
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

public class IngredientRepositoryTest {

    private IngredientRepository repository;
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

        try {
            server.start();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        final HttpUrl baseUrl = server.url("/");
        final Context context = InstrumentationRegistry.getTargetContext();
        final ServiceLocator serviceLocator = new ServiceLocatorForTest(context, baseUrl.toString());
        repository = new IngredientRepository(serviceLocator.getRecipeService(), serviceLocator.getIngredientDao());
    }

    @Test
    public void shouldBeCorrectIngredients() throws Exception {

        // when
        final Resource<List<IngredientEntity>> ingredientsResource =
                LiveDataTestUtil.getValue(repository.getIngredients("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);

        // then
        assertThat(ingredientsResource, is(notNullValue()));
        assertThat(ingredientsResource.status, is(Resource.Status.SUCCESS));
        assertThat(ingredientsResource.data, is(notNullValue()));
        assertThat(ingredientsResource.data.size(), is(11));

    }

    @Test
    public void shouldBeRemoveOrphan() throws InterruptedException {
        // given
        LiveDataTestUtil.getValue(repository.getIngredients("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);
        final List<String> recipeIds = Arrays.asList("123456789");

        // when
        int count = repository.deleteOrphan(recipeIds);

        // then
        assertThat(count, is(0));
    }

    @Test
    public void shouldBeUpdateIngredients() throws InterruptedException {
        // given
        final Resource<List<IngredientEntity>> list =
                LiveDataTestUtil.getValue(repository.getIngredients("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);

        // when
        int count = repository.updateAllDataInDatabase("c0e5582e-252f-4e94-8a49-e12b4b047afb", createIngredients());
        assertThat(count, is(2));
    }

    private List<IngredientEntity> createIngredients() {
        final IngredientEntity entity_1 = new IngredientEntity();
        entity_1.setPortion("1");
        entity_1.setDescription("Tomaten");
        entity_1.setComment("Bio");
        entity_1.setRecipeId("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        final IngredientEntity entity_2 = new IngredientEntity();
        entity_2.setPortion("2");
        entity_2.setDescription("Aubergine");
        entity_2.setComment("Bio");
        entity_2.setRecipeId("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        return Arrays.asList(entity_1, entity_2);
    }
}
