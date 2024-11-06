package dev.vality.dominator.handler.get;

public interface GetTermSetsHandler<T, R> {

    R handle(T query);

}
