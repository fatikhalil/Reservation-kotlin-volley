package ma.ensa.reservation.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.160:8082/"; // Remplacez par l'URL de votre backend
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Créer un interceptor pour mesurer la taille des données
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Niveau BODY pour voir les détails des requêtes

            // Créer un interceptor personnalisé pour mesurer la taille des données reçues
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                okhttp3.Request request = chain.request();
                okhttp3.Response response = chain.proceed(request);

                // Mesurer la taille des données reçues
                long contentLength = response.body().contentLength(); // Taille en octets
                double sizeInKB = contentLength / 1024.0; // Convertir en KB

                System.out.println("Taille des données reçues : " + sizeInKB + " KB");

                return response;
            });

            // Ajouter le logging interceptor (optionnel, pour le débogage)
            httpClient.addInterceptor(loggingInterceptor);

            // Configurer Retrofit avec OkHttpClient personnalisé
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}