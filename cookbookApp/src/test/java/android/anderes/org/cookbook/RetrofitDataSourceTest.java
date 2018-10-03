package android.anderes.org.cookbook;

import android.anderes.org.cookbook.infrastructure.Recipe;
import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

public class RetrofitDataSourceTest {

    private MockWebServer server;
    private Retrofit retrofit;

    @Before
    public void setup() {
        server = new MockWebServer();
        try {
            server.start();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(server.getHostName())
                .port(server.getPort())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(httpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Test
    public void shouldBeFetchCollection() {
        // given
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("[{\"title\":\"Apfel-Mascarpone-Creme mit Amarettini\",\"id\":\"4ab99cc8-b21a-4146-97ef-a7949184a173\",\"editingDate\":1487459207052}," +
                        "{\"title\":\"Arabische Pasta\",\"id\":\"c0e5582e-252f-4e94-8a49-e12b4b047afb\",\"editingDate\":1515082740753}]"));
        // when
        final RecipeService service = retrofit.create(RecipeService.class);
        final List<RecipeAbstract> list = service.getRecipeAbstract().blockingSingle();
        // then
        assertThat(list, is(not(nullValue())));
        assertThat(list.size(), is(2));
        for (RecipeAbstract r : list) {
            assertThat(r.getId(), is(not(nullValue())));
            assertThat(r.getTitle(), is(not(nullValue())));
            assertThat(r.getTitle().length(), is(greaterThan(1)));
            assertThat(r.getEditingDate(), is(not(nullValue())));
            assertThat(r.getEditingDate(), is(greaterThan(1L)));
        }
    }

    @Test
    public void shouldBeFetchItem() {
        // given
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{\"id\":\"c0e5582e-252f-4e94-8a49-e12b4b047afb\",\"noOfPeople\":\"2\",\"ingredients\":[{\"description\":\"Curry\",\"portion\":null,\"comment\":null},{\"description\":\"getrocknete Feigen oder Datteln\",\"portion\":\"4-8 Stk.\",\"comment\":\"inStreifen\"},{\"description\":\"Zwiebel\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Kokosnussmilch\",\"portion\":\"250 mL\",\"comment\":null},{\"description\":\"Penne\",\"portion\":\"250\",\"comment\":null},{\"description\":\"Tofu\",\"portion\":\"250 g\",\"comment\":null},{\"description\":\"Knoblizehen\",\"portion\":\"2\",\"comment\":null},{\"description\":\"Salz, Pfeffer und Rapsöl\",\"portion\":null,\"comment\":null},{\"description\":\"Kreuzkümmel\",\"portion\":null,\"comment\":\"frisch gemörsert\"},{\"description\":\"Peperoncini\",\"portion\":\"1\",\"comment\":null},{\"description\":\"Korianderblätter\",\"portion\":\"½ Bund\",\"comment\":null}],\"tags\":[\"pasta\",\"fleischlos\"],\"title\":\"Arabische Pasta\",\"preparation\":\"<p>Tofu in Würfel schneiden und mit etwas Öl anbraten. Kurz befor der Tofu aus der Pfanne genommen wird, mit Curry und Kreuzkümmel bestreuen und den Tofu kurz duchschwenken. Danach aus der Pfanne nehmen.<br />\\n<br />\\nWasser aufsetzen, salzen und Pasta al dente kochen.<br />\\n<br />\\nIn der Zwischenzeit die in Streifen geschnittenen Zwiebel kurz anbraten, anschlissend den in Streifen geschnittenen Peperoncini dazu geben. Etwas später die Knoblizehen (in Würfelchen) in die Pfanne geben und mit Salz würzen. Anschliessend die Kokosnussmilch dazu geben, kurz aufkochen lassen.&nbsp;Dann Temperatur zurücknehmen. Zu langes Kochen tut der Kokosnussmilch nicht gut. Ca. 4 Minuten bevor die Pasta al dente sind den Tofu in die Sauce geben und nur noch warm werden lassen. Kurz bevor die Spaghetti mit der Sauce vermischt wird, die getrockneten Feigen (in Streifen geschnitten) in die Sauce geben.</p>\\n\\n<p>Die Sauce und die Spaghetti nach dem mischen zugedeckt ein paar Minuten ziehen lassen. Zum Servieren noch etwas frisch gemahlener Pfeffer und ein paar Korianderblätter über die Pasta geben.<br />\\n&nbsp;</p>\\n\",\"preamble\":\"<p>Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (getrocknete Feigen oder Datteln) gemischt wird, habe ich diese Rezept \\\"Arabische Pasta\\\" benannt.</p>\\n\",\"rating\":5,\"addingDate\":1250959818424,\"editingDate\":1515082740753,\"image\":null}"));
        // when
        final RecipeService service = retrofit.create(RecipeService.class);
        final Recipe recipe = service.getRecipe("c0e5582e-252f-4e94-8a49-e12b4b047afb").blockingSingle();
        // then
        assertThat(recipe, is(not(nullValue())));
        assertThat(recipe.getId(), is("c0e5582e-252f-4e94-8a49-e12b4b047afb"));
        assertThat(recipe.getIngredients(), is(not(nullValue())));
        assertThat(recipe.getIngredients().size(), is(11));
        assertThat(recipe.getTags(), is(not(nullValue())));
        assertThat(recipe.getTags().size(), is(2));
    }
}
