package rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import rangiffler.api.context.CookieHolder;
import rangiffler.api.context.SessionStorageHolder;
import rangiffler.api.interceptops.AddCookiesReqInterceptor;
import rangiffler.api.interceptops.ExtractCodeFromRespInterceptor;
import rangiffler.api.interceptops.ReceivedCookieRespInterceptor;
import rangiffler.config.Config;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RangifflerAuthClient {

    private static final Config CFG = Config.getConfig();

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .followRedirects(true)
            .addNetworkInterceptor(new ReceivedCookieRespInterceptor())
            .addNetworkInterceptor(new AddCookiesReqInterceptor())
            .addNetworkInterceptor(new ExtractCodeFromRespInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(CFG.authUrl())
            .build();

    private final RangifflerAuthApi rangifflerAuthApi = retrofit.create(RangifflerAuthApi.class);

    public void authorize() throws Exception {
        SessionStorageHolder.getInstance().init();
        rangifflerAuthApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                SessionStorageHolder.getInstance().getCodeChallenge(),
                "S256"
        ).execute();
    }

    public Response<Void> login(String username, String password) throws Exception {
        return rangifflerAuthApi.login(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username,
                password
        ).execute();
    }


    public JsonNode getToken() throws Exception {
        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        return rangifflerAuthApi.getToken(
                basic,
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                SessionStorageHolder.getInstance().getCode(),
                SessionStorageHolder.getInstance().getCodeVerifier()
        ).execute().body();
    }

    public Response<Void> register(String username, String password) throws Exception {
        return rangifflerAuthApi.register(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username,
                password,
                password
        ).execute();
    }
}
