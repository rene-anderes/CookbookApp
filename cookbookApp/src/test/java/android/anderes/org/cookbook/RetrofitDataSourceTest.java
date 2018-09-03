package android.anderes.org.cookbook;

import android.anderes.org.cookbook.infrastructure.RecipeAbstract;
import android.anderes.org.cookbook.infrastructure.RecipeService;

import org.hamcrest.Matchers;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

public class RetrofitDataSourceTest {

    private MockWebServer server;
    private Retrofit retrofit;

    @Before
    public void setup() {

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
    public void shouldBeFetchRemoteData() {

        final RecipeService service = retrofit.create(RecipeService.class);
        final List<RecipeAbstract> list = service.getRecipeAbstract().blockingSingle();
        assertThat(list, is(not(Matchers.nullValue())));
        assertThat(list.size(), is(2));
    }
}
