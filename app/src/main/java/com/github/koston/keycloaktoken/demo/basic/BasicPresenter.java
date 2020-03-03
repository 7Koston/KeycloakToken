package com.github.koston.keycloaktoken.demo.basic;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class BasicPresenter {
  private final CompositeDisposable compositeDisposable;

  public BasicPresenter() {
    compositeDisposable = new CompositeDisposable();
  }

  public void onDestroy() {
    disposeAll();
  }

  private void disposeAll() {
    if (!compositeDisposable.isDisposed()) {
      compositeDisposable.clear();
    }
  }

  protected void addSubscription(Disposable subscription) {
    compositeDisposable.add(subscription);
  }
}
