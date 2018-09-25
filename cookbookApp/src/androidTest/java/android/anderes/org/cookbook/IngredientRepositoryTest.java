package android.anderes.org.cookbook;

import android.anderes.org.cookbook.database.IngredientEntity;
import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.repository.IngredientRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

public class IngredientRepositoryTest {

    private MockWebServer server;
    private ServiceLocator serviceLocator;
    // Use this rule to instantly execute any background operation on the calling thread.
    @Rule // Stellt sicher, dass für LiveDate der richtige Thread verwendet wird
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        server = new MockWebServer();
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
        serviceLocator = new ServiceLocatorForTest(context, baseUrl.toString());
    }

    @Test
    public void shouldBeCorrectIngredients() throws Exception {
        // given
        final IngredientRepository repository = new IngredientRepository(serviceLocator.getRecipeService(), serviceLocator.getIngredientDao());

        // when
        final Resource<List<IngredientEntity>> ingredientsResource =
                LiveDataTestUtil.getValue(repository.getIngredients("c0e5582e-252f-4e94-8a49-e12b4b047afb"), 2);

        // then
        assertThat(ingredientsResource, is(notNullValue()));
        assertThat(ingredientsResource.status, is(Resource.Status.SUCCESS));
        assertThat(ingredientsResource.data, is(notNullValue()));
        assertThat(ingredientsResource.data.size(), is(11));

    }
}
