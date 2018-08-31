package android.anderes.org.cookbook;

import android.anderes.org.cookbook.infrastructure.RemoteDataRecipeAbstract;
import android.anderes.org.cookbook.infrastructure.MyResources;
import android.anderes.org.cookbook.infrastructure.RemoteDataSource;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;
import static org.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class RemoteDataSourceTest {

    private Context appContext;
    private MyResources appResources;
    private RemoteDataRecipeAbstract remoteDataRecipeAbstract;
    private MockWebServer server;

    @Before
    public void setup() {

        appContext = InstrumentationRegistry.getTargetContext();
        appResources = new MyResources(appContext);
        server = new MockWebServer();
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

    @Test
    public void shouldBeRecipeAbstracCollection() {
        // given
        final HttpUrl baseUrl = server.url("/resources-api/recipes");

        final RemoteDataSource remote = new RemoteDataSource(appResources, baseUrl.toString());
        remoteDataRecipeAbstract = new RemoteDataRecipeAbstract();

        // when
        remote.getRecipeAbstractCollection(remoteDataRecipeAbstract);
        // then
        await().until(userRepositorySize(), equalTo(1));


    }

    private Callable<Integer> userRepositorySize() {
        return new Callable<Integer>() {
            public Integer call() throws Exception {
                return remoteDataRecipeAbstract.getData().size();
            }
        };
    }
}
