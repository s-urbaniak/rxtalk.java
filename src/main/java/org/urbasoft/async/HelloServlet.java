package org.urbasoft.async;

import com.ebay.rx.StringFunctions;
import com.ebay.rx.ning.HttpResponseBodyParts;
import com.ebay.rx.ning.NingObservable;
import com.ning.http.client.AsyncHttpClient;
import rx.Observable;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

@WebServlet(value = "/async", asyncSupported = true)
public class HelloServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        AsyncHttpClient client = new AsyncHttpClient();

        Observable<String> obs1 = NingObservable.createChunked(client.prepareGet("http://localhost:6161/obs1?it=30&delay=100&jitter=1000"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> obs2 = NingObservable.createChunked(client.prepareGet("http://localhost:6060/obs2?it=30&delay=100&jitter=1000"))
                .map(HttpResponseBodyParts.toString);

        Observable<String> bodies = Observable
                .mergeDelayError(obs1, obs2)
                .map(StringFunctions.decorateWithThreadName);

        res.getWriter().println("<html><body>");
        res.getWriter().println("<div>Starting thread: " + Thread.currentThread().getName() + "</div>");
        res.getWriter().flush();

        SlowServerObserver obs = SlowServerObserver.fromContext(req.startAsync());
        bodies.subscribe(obs);
    }
}
