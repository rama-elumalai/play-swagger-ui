package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import util.SwaggerJsonUtil;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SwaggerController extends Controller {

    public Result getJson() {
        SwaggerJsonUtil swaggerJsonUtil = new SwaggerJsonUtil();
        return ok(swaggerJsonUtil.parseRouterFiles());
    }
}
