package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StockTickerRxPublisher {

    private final Flowable<ApplicationTemplateRoute> publisher;

    public StockTickerRxPublisher() {
        Observable<ApplicationTemplateRoute> stockPriceUpdateObservable = Observable.create(emitter -> {

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newStockTick(emitter), 0, 2, TimeUnit.SECONDS);

        });

        ConnectableObservable<ApplicationTemplateRoute> connectableObservable = stockPriceUpdateObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private Runnable newStockTick(ObservableEmitter<ApplicationTemplateRoute> emitter) {
        return () -> {
            List<ApplicationTemplateRoute> stockPriceUpdates = getUpdates(5);
            if (stockPriceUpdates != null) {
                emitStocks(emitter, stockPriceUpdates);
            }
        };
    }

    private void emitStocks(ObservableEmitter<ApplicationTemplateRoute> emitter, List<ApplicationTemplateRoute> stockPriceUpdates) {
        for (ApplicationTemplateRoute stockPriceUpdate : stockPriceUpdates) {
            try {
                emitter.onNext(stockPriceUpdate);
            } catch (RuntimeException e) {
                log.error("Cannot send StockUpdate", e);
            }
        }
    }

    public Flowable<ApplicationTemplateRoute> getPublisher() {
        return publisher;
    }

    public Flowable<ApplicationTemplateRoute> getPublisher(List<String> stockCodes) {
        if (stockCodes != null) {
            return publisher.filter(stockPriceUpdate -> true/*stockCodes.contains(stockPriceUpdate.getStockCode())*/);
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