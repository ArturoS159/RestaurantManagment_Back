package com.przemarcz.order.util;

import com.przemarcz.order.model.RestaurantPayment;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

@Component
public class PaymentHelper {

    private static final int CORRECT_RESPONSE_200 = 200;
    private static final int CORRECT_RESPONSE_302 = 302;
    private static final int EIGHT = 8;
    private static final char AMPERSAND = '&';


    public String getAuthorizationToken(String clientId, String clientSecret){
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(
                String.format("https://secure.snd.payu.com/pl/standard/user/oauth/authorize?grant_type=client_credentials&client_id=%s&client_secret=%s",clientId,clientSecret));
        Response response = target
                .request()
                .post(null);
        String body = response.readEntity(String.class);
        response.close();
        if(response.getStatus()== CORRECT_RESPONSE_200){
            return getTokenFromBody(body);
        }
        throw new IllegalArgumentException("Bad token details!");
    }

    private String getTokenFromBody(String body){
        JSONObject jsonObject = new JSONObject(body);
        return jsonObject.getString("token_type").concat(" ").concat(jsonObject.getString("access_token"));
    }

    public PaymentResponse pay(Payment payment, RestaurantPayment restaurantPayment) throws IOException {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Entity<String> payload = Entity.json(objectMapper.writeValueAsString(payment));
        Response response = client.target("https://secure.snd.payu.com/api/v2_1/orders")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", getAuthorizationToken(restaurantPayment.getClientId(),restaurantPayment.getClientSecret()))
                .post(payload);
        if(response.getStatus() == CORRECT_RESPONSE_302){
            return new PaymentResponse(getOrderIdFromUrl(response.getLocation()),response.getLocation().toString());
        }
        return null;
    }

    private String getOrderIdFromUrl(URI url){
        String urlString = url.getRawQuery();
        return urlString.substring(EIGHT,urlString.indexOf(AMPERSAND));
    }

}
