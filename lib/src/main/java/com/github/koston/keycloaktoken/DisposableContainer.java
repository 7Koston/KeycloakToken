/*
 * Copyright 2019 7Koston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.koston.keycloaktoken;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class DisposableContainer {

  private final CompositeDisposable compositeDisposable;

  public DisposableContainer() {
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
