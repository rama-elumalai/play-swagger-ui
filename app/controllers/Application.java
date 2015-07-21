package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
