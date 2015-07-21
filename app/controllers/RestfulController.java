package controllers;

import domain.JsonResponse;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Named;
import javax.inject.Singleton;


/**
 * Created by relumalai on 7/14/15.
 */
@Named
@Singleton
public class RestfulController extends Controller {

    public Result getJsonResponse() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setDescription("test");
        jsonResponse.setName("test");
        return ok(Json.toJson(jsonResponse));
    }

    public Result getJsonResponseWithQueryParam(String param) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setDescription("test");
        jsonResponse.setName(param);
        return ok(Json.toJson(jsonResponse));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result postJsonResponse() {
        JsonResponse jsonResponse = Json.fromJson(request().body()
                .asJson(), JsonResponse.class);
        return ok(Json.toJson(jsonResponse));
    }

}
