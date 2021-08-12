package main.java.me.avankziar.general.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MultiMap<K, V> //implements Map<K, V>//, Collection<K>, Comparable<K>, Serializable
{
	private final LinkedHashMap<Collection<K>, Collection<V>> map = new LinkedHashMap<>();
	private Collection<K> keys = new ArrayList<>();
	private Collection<V> values = new ArrayList<>();
	
	public Collection<V> get(Collection<K> key)
	{
		return map.getOrDefault(key, Collections.<V> emptyList());
	}
	
	public void put(Collection<K> key, Collection<V> value)
	{
		map.computeIfAbsent(key, k -> new ArrayList<>()).addAll(value);
	}
	
	public MultiMap<K, V> setKeys(K...keys)
	{
		for(K k : keys)
		{
			this.keys.add(k);
		}
		return this;
	}
	
	public MultiMap<K, V> setValues(V...values)
	{
		for(V v : values)
		{
			this.values.add(v);
		}
		put(this.keys, this.values);
		this.keys = new ArrayList<>();
		this.values = new ArrayList<>();
		return this;
	}
	
	@Override
	public String toString() 
	{
        /*Iterator<Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key   == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }*/
		return null;
    }
}