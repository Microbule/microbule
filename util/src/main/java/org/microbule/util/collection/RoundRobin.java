package org.microbule.util.collection;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

public class RoundRobin<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<T> items;
    private final AtomicInteger index = new AtomicInteger();
    private final int n;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RoundRobin(List<T> items) {
        this.items = items == null ? Collections.emptyList() : Lists.newArrayList(items);
        this.n = this.items.size();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public T nextItem() {
        return n == 0 ? null : items.get(index.getAndUpdate(val -> (val + 1) % n));
    }
}
