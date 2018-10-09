package org.anderes.app.cookbook;

import org.anderes.app.cookbook.database.RecipeAbstractEntity;
import org.anderes.app.cookbook.repository.RecipeAbstractRepository;
import org.anderes.app.cookbook.repository.Resource;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class RecipeAbstractRepositoryNegativeTest {

    private MockWebServer server;
    private RecipeAbstractRepository repository;
    // Use this rule to instantly execute any background operation on the calling thread.
    @Rule // Stellt sicher, dass für LiveDate der richtige Thread verwendet wird
    public InstantTaskExecutorRule testRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(403));
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

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldBeDataError() throws InterruptedException {

        // when
        final Resource<List<RecipeAbstractEntity>> recipesResource =
                LiveDataTestUtil.getValue(repository.getRecipes(), 2);

        // then
        assertThat(recipesResource, is(notNullValue()));
        assertThat(recipesResource.status, is(Resource.Status.ERROR));
        assertThat(recipesResource.data, is(notNullValue()));
        assertThat(recipesResource.data.size(), is(0));

    }

    @Test(expected = IOException.class)
    public void shouldBeRecipeCollectionFromRemoteDataService() throws IOException {

        // when
        repository.getRecipeCollectionFromRemote();

        // then throw exception
    }
}
