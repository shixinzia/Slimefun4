package com.xzavier0722.mc.plugin.slimefun4.storage.controller;

import com.xzavier0722.mc.plugin.slimefun4.storage.common.ScopeKey;

public class LinkedKey extends ScopeKey {
    private final ScopeKey self;
    private ScopeKey parent;

    public LinkedKey(ScopeKey self) {
        super(self.getScope());
        this.self = self;
    }

    public LinkedKey(ScopeKey parent, ScopeKey self) {
        this(self);
        this.parent = parent;
    }

    public void setParent(ScopeKey parent) {
        this.parent = parent;
    }

    public ScopeKey getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        return self.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return self.equals(obj instanceof LinkedKey linked ? linked.self : obj);
    }
}
