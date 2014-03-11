package org.urbasoft.async;

import rx.Observer;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;

public final class SlowServerObserver implements Observer<String> {

    private final PrintWriter writer;

    private final AsyncContext ctx;

    public SlowServerObserver(AsyncContext ctx) throws IOException {
        this.ctx = ctx;
        this.writer = ctx.getResponse().getWriter();
    }

    @Override
    public void onNext(String chunk) {
        writer.println("<div>" + chunk + "</div>");
        writer.flush();
    }

    @Override
    public void onError(Throwable t) {
        writer.println("<div>Gosh, an error happened!<br><pre>");
        t.printStackTrace(writer);
        writer.println("</pre></div>");

        onCompleted();
    }

    @Override
    public void onCompleted() {
        writer.println("<div>Finishing thread: " + Thread.currentThread().getName() + "</div>");
        writer.println("</body></html>");
        ctx.complete();
    }

    public static SlowServerObserver fromContext(AsyncContext ctx) throws IOException {
        return new SlowServerObserver(ctx);
    }

}
