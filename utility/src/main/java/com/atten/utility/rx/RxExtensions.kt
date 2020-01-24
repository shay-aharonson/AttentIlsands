package com.atten.utility.rx

import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Handy Rxlifecycle extensions
 */
fun <T : Any> Single<T>.bindUntil(
    lifecycle: Observable<ActivityEvent>,
    event: ActivityEvent = ActivityEvent.DESTROY
): Observable<T> {
    return this.toObservable().takeUntil(lifecycle.takeFirst { it == event })
}

fun <T : Any> Observable<T>.takeFirst(predicate: (nextItem: T) -> Boolean): Observable<T> {
    return this.takeUntil { predicate(it) }.takeLast(1)
}