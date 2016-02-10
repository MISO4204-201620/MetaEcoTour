package controllers;

import models.Product;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.products.details;
import views.html.products.list;

import java.util.List;

public class Products extends Controller {

    private static final Form<Product> productForm = Form.form(Product.class);

    @Transactional(readOnly=true)
    public Result list() {
        List<Product> products = JPA.em().createNamedQuery("Product.findAll", Product.class ).getResultList();
        return ok(list.render(products));
    }

    @Transactional(readOnly=true)
    public Result listJson() {
        List<Product> products = JPA.em().createNamedQuery("Product.findAll", Product.class ).getResultList();
        return ok(Json.toJson(products));
    }

    public Result newProduct() {
        session("idProduct","-1");
        return ok(details.render(productForm));
    }

    @Transactional (readOnly=true)
    public Result details(String id) {
        Product product = JPA.em().find(Product.class, Long.valueOf(id).longValue());

        if (product == null) {
            return notFound(String.format("Product %s does not exist.", id));
        }
        session("idProduct",id);
        Form<Product> filledForm = productForm.fill(product);
        return ok(details.render(filledForm));
    }


    @Transactional
    public Result save() {
        Form<Product> boundForm = productForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(details.render(boundForm));
        }

        Product product = boundForm.get();
        String idProduct = session().get("idProduct");
        //idProduct null

        Product productTmp = null;
        if (idProduct != null ){
            Long idProductL = Long.parseLong(idProduct);
            productTmp = JPA.em().find(Product.class, Long.parseLong(idProduct));
        }

        if (productTmp == null){
            JPA.em().persist(product);
            flash("success",
                    String.format("Successfully added product %s", product));
        } else {
            productTmp.setDescription(product.getDescription());
            productTmp.setEan(product.getEan());
            productTmp.setName(product.getName());

            JPA.em().merge(productTmp);
            flash("success",
                    String.format("Successfully updated product %s", product));
        }

        return redirect(routes.Products.list());
    }

    @Transactional
    public Result delete(String id) {
        Product product = JPA.em().find(Product.class, Long.valueOf(id).longValue());

        if (product == null) {
            return notFound(String.format("Product %s does not exist.", id));
        }
        JPA.em().remove(product);

        return redirect(routes.Products.list());
    }
}