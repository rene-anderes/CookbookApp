package android.anderes.org.cookbook;

import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.model.Resource;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.annotation.MainThread;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class RecipeAbstractRepositoryTest {

    private MockWebServer server;
    @Rule // Stellt sicher, dass für LiveDate der richtige Thread verwendet wird
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        server = new MockWebServer();
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("[{\"title\":\"Apfel-Mascarpone-Creme mit Amarettini\",\"id\":\"4ab99cc8-b21a-4146-97ef-a7949184a173\",\"editingDate\":1487459207052}]"));
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("[{\"title\":\"Apfel-Mascarpone-Creme mit Amarettini\",\"id\":\"4ab99cc8-b21a-4146-97ef-a7949184a173\",\"editingDate\":1487459207052}]"));
        try {
            server.start();
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test @MainThread
    public void shouldBeRecipeCollection() throws InterruptedException {
        // given
        // given
        final HttpUrl baseUrl = server.url("/resources-api/recipes/");
        final RecipeAbstractRepository repository = new RecipeAbstractRepository(baseUrl.toString());
        // when
        final Resource<List<RecipeAbstract>> recipesResource = LiveDataTestUtil.getValue(repository.getRecipes());
        // then
        assertThat(recipesResource, is(notNullValue()));
        assertThat(recipesResource.data, is(notNullValue()));
    }
}
