package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StockTickerReactorPublisher {

    private final Flux<ApplicationTemplateRoute> publisher;

    public StockTickerReactorPublisher() {
        Flux<ApplicationTemplateRoute> stockPriceUpdateFlux = Flux.create(emitter -> {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newStockTick(emitter), 0, 2, TimeUnit.SECONDS);
        }, FluxSink.OverflowStrategy.BUFFER);
        ConnectableFlux<ApplicationTemplateRoute> connectableFlux = stockPriceUpdateFlux.share().publish();
        connectableFlux.connect();

        publisher = Flux.from(connectableFlux);
    }

    private Runnable newStockTick(FluxSink<ApplicationTemplateRoute> emitter) {
        return () -> {
            List<ApplicationTemplateRoute> stockPriceUpdates = getUpdates(5);
            if (stockPriceUpdates != null) {
                emitStocks(emitter, stockPriceUpdates);
            }
        };
    }

    private void emitStocks(FluxSink<ApplicationTemplateRoute> emitter, List<ApplicationTemplateRoute> stockPriceUpdates) {
        for (ApplicationTemplateRoute stockPriceUpdate : stockPriceUpdates) {
            try {
                emitter.next(stockPriceUpdate);
            } catch (RuntimeException e) {
                log.error("Cannot send StockUpdate", e);
            }
        }
    }

    public Flux<ApplicationTemplateRoute> getPublisher() {
        return publisher;
    }

    public Flux<ApplicationTemplateRoute> getPublisher(List<String> stockCodes) {
        if (stockCodes != null) {
            return publisher.filter(stockPriceUpdate -> true/*stockCodes.contains(stockPriceUpdate.getStockCode()*/);
        }
        return publisher;
    }

    private List<ApplicationTemplateRoute> getUpdates(int number) {
        List<ApplicationTemplateRoute> updates = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            updates.add(rollUpdate());
        }
        return updates;
    }

    private ApplicationTemplateRoute rollUpdate() {
        return ApplicationTemplateRoute.builder().build();
    }

}
