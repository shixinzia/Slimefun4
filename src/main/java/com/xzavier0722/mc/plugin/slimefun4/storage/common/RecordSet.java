package com.xzavier0722.mc.plugin.slimefun4.storage.common;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.DataUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordSet {
    private final Map<FieldKey, String> data;
    private boolean readonly = false;

    public RecordSet() {
        data = new HashMap<>();
    }

    @ParametersAreNonnullByDefault
    public void put(FieldKey key, String val) {
        checkReadonly();
        data.put(key, val);
    }

    @ParametersAreNonnullByDefault
    public void put(FieldKey key, ItemStack itemStack) {
        checkReadonly();
        data.put(key, DataUtils.itemStack2String(itemStack));
    }

    public void put(FieldKey key, boolean val) {
        put(key, val ? "1" : "0");
    }

    @ParametersAreNonnullByDefault
    public Map<FieldKey, String> getAll() {
        return Collections.unmodifiableMap(data);
    }

    @ParametersAreNonnullByDefault
    public String get(FieldKey key) {
        return data.get(key);
    }

    @ParametersAreNonnullByDefault
    public String getOrDef(FieldKey key, String def) {
        return data.getOrDefault(key, def);
    }

    @ParametersAreNonnullByDefault
    public int getInt(FieldKey key) {
        return Integer.parseInt(data.get(key));
    }

    @ParametersAreNonnullByDefault
    public ItemStack getItemStack(FieldKey key) {
        return DataUtils.string2ItemStack(data.get(key));
    }

    public boolean getBoolean(FieldKey key) {
        return getInt(key) == 1;
    }

    public void readonly() {
        readonly = true;
    }

    private void checkReadonly() {
        if (readonly) {
            throw new IllegalStateException("RecordSet cannot be modified after readonly() called.");
        }
    }

}
