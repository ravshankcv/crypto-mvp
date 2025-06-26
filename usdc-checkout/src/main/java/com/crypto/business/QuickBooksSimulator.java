package com.crypto.business;

import okhttp3.*;
import java.io.IOException;

public class QuickBooksSimulator {

  private static final String ACCESS_TOKEN = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwieC5vcmciOiJIMCJ9";
  private static final String REALM_ID = "9341454832068749";
  private static final String BASE_URL = "https://sandbox-quickbooks.api.intuit.com/v3/company/";

  public static void syncToQuickBooks(String category, String vendor, double amount) throws IOException {
    String url = BASE_URL + REALM_ID + "/purchase?minorversion=65";

    String jsonPayload = """
        {
          "AccountRef": {
            "value": "35",
            "name": "Bank"
          },
          "PaymentType": "Cash",
          "EntityRef": {
            "value": "42",
            "type": "Vendor"
          },
          "Line": [
            {
              "Amount": %f,
              "DetailType": "AccountBasedExpenseLineDetail",
              "AccountBasedExpenseLineDetail": {
                "AccountRef": {
                  "value": "87",
                  "name": "%s"
                }
              }
            }
          ]
        }
        """.formatted(amount, category);

    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

    Request request = new Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .post(body)
        .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("QuickBooks Response: " + response.code());
      if (response.body() != null)
        System.out.println(response.body().string());
    }
  }
}
