package org.example.collectionslab;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsLabTest {
    @Test
    void listsCoverIndexedAndDequeOperations() {
        List<String> array = new ArrayList<>(List.of("a", "b", "c"));
        LinkedList<String> linked = new LinkedList<>(List.of("b", "c"));
        array.add(1, "x");
        linked.addFirst("a");
        assertEquals(List.of("a", "x", "b", "c"), array);
        assertEquals("a", linked.removeFirst());
        assertEquals("c", linked.getLast());
    }

    @Test
    void vectorAndCopyOnWriteListHaveDifferentConcurrencyTradeoffs() {
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        assertEquals(1, vector.firstElement());
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("Ada", "Linus"));
        Iterator<String> snapshot = list.iterator();
        list.add("Grace");
        List<String> seen = new ArrayList<>();
        snapshot.forEachRemaining(seen::add);
        assertEquals(List.of("Ada", "Linus"), seen);
        assertEquals(3, list.size());
    }

    @Test
    void setsChooseUniquenessPlusOrder() {
        assertEquals(2, new HashSet<>(List.of(3, 1, 3)).size());
        assertEquals(List.of(3, 1, 2), new ArrayList<>(new LinkedHashSet<>(List.of(3, 1, 2, 1))));
        assertEquals(List.of(1, 2, 3), new ArrayList<>(new TreeSet<>(List.of(3, 1, 2, 1))));
    }

    @Test
    void queueAndDequeApplyTheirOwnRemovalRules() {
        PriorityQueue<Integer> priority = new PriorityQueue<>(List.of(3, 1, 2));
        assertEquals(List.of(1, 2, 3), List.of(priority.poll(), priority.poll(), priority.poll()));
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("queue-first"); deque.addFirst("stack-top");
        assertEquals("stack-top", deque.removeFirst());
        assertEquals("queue-first", deque.removeFirst());
        assertThrows(NullPointerException.class, () -> deque.add(null));
    }

    @Test
    void mapsCoverHashOrderSortLegacyAndConcurrentOperations() {
        Map<String, Integer> hash = new HashMap<>();
        hash.put(new String("id"), 1);
        assertEquals(1, hash.get("id"));
        LinkedHashMap<Integer, String> insertion = new LinkedHashMap<>();
        insertion.put(2, "b"); insertion.put(1, "a");
        assertEquals(List.of(2, 1), new ArrayList<>(insertion.keySet()));
        assertEquals(List.of(1, 2), new ArrayList<>(new TreeMap<>(insertion).keySet()));
        assertThrows(NullPointerException.class, () -> new Hashtable<String, Integer>().put(null, 1));
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        counts.merge("book", 1, Integer::sum); counts.merge("book", 1, Integer::sum);
        assertEquals(2, counts.get("book"));
        assertThrows(NullPointerException.class, () -> counts.put("book", null));
    }

    @Test
    void iteratorIsFailFastForUnexpectedStructuralModification() {
        List<Integer> values = new ArrayList<>(List.of(1, 2));
        Iterator<Integer> iterator = values.iterator();
        values.add(3);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void customHashMapHandlesCollisionReplacementAndResize() {
        SimpleHashMap<Key, String> map = new SimpleHashMap<>();
        map.put(new Key("one"), "first"); map.put(new Key("two"), "second");
        assertEquals("second", map.get(new Key("two")));
        assertEquals("first", map.put(new Key("one"), "updated"));
        for (int i = 0; i < 20; i++) map.put(new Key("k" + i), "v" + i);
        assertEquals("updated", map.get(new Key("one")));
        assertTrue(map.capacityForTest() > 16);
    }

    @Test
    void lruCacheEvictsOldestAccessAfterReadPromotesAnEntry() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("a", 1); cache.put("b", 2); cache.get("a"); cache.put("c", 3);
        assertTrue(cache.get("a").isPresent());
        assertTrue(cache.get("b").isEmpty());
        assertEquals(3, cache.get("c").orElseThrow());
    }

    @Test
    void ringBufferWrapsAndRejectsOverflow() {
        RingBuffer<Integer> buffer = new RingBuffer<>(3);
        assertTrue(buffer.offer(1)); assertTrue(buffer.offer(2)); assertTrue(buffer.offer(3));
        assertFalse(buffer.offer(4));
        assertEquals(1, buffer.poll()); assertTrue(buffer.offer(4));
        assertIterableEquals(List.of(2, 3, 4), buffer);
    }

    private record Key(String value) {
        @Override public int hashCode() { return 7; }
    }
}
