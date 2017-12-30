package org.kucro3.keleton.implementation.loader;

import org.kucro3.keleton.implementation.exception.KeletonLoaderException;

import java.util.*;

class KeletonLoaderTree {
    KeletonLoaderTree(Collection<KeletonLoadedModule> modules) throws KeletonLoaderException
    {
        this.dependcies = new HashMap<>();
        this.mapped = new HashMap<>();
        this.modules = new HashMap<>();

        Set<String> deps;
        for(KeletonLoadedModule module : modules)
        {
            this.modules.put(module.getModule().name(), module);
            this.dependcies.put(module.getModule().name(), deps = new HashSet<>(Arrays.asList(module.getModule().dependencies())));
            if(deps.isEmpty())
                new Node(module).linkAfter(tail);
        }

        if(head == null)
            throw new KeletonLoaderException("Missing framework? Root dependency not found");
    }

    void compute() throws KeletonLoaderException
    {
        for(KeletonLoadedModule module : this.modules.values())
            compute(module, dependcies.get(module.getModule().name()));
    }

    void compute(KeletonLoadedModule from, Set<String> deps) throws KeletonLoaderException
    {
        if(mapped.containsKey(from.getModule().name()))
            return;

        Node last = head;

        for(String dep : deps)
        {
            if(dependcies.get(dep).contains(from.getModule().name()))
                throw new KeletonLoaderException("Dependency loop detected");

            Node n = mapped.get(dep);

            if (n != null)
                last = max(last, n);
            else
            {
                KeletonLoadedModule module = modules.get(dep);
                Set<String> mdeps = dependcies.get(dep);
                compute(module, mdeps);
            }
        }

        modCount++;
        new Node(from).linkAfter(last);
    }

    KeletonLoaderPipeline build()
    {
        ArrayList<KeletonLoadedModule> modules = new ArrayList<>(this.modules.size());

        Node n = head;
        while(n != null)
        {
            modules.add(n.module);
            n = n.next;
        }

        return new KeletonLoaderPipeline(modules);
    }

    static Node max(Node a, Node b)
    {
        if(b.index.get() > a.index.get())
            return b;
        return a;
    }

    private final Map<String, KeletonLoadedModule> modules;

    private final Map<String, Set<String>> dependcies;

    private final Map<String, Node> mapped;

    private Node head;

    private Node tail;

    private int modCount;

    private int size;

    class Node
    {
        Node(KeletonLoadedModule module)
        {
            this.module = module;
        }

        void linkAfter(Node node)
        {
            if(tail == null)
                tail = this;

            if(node == null)
            {
                head = this;
                index = new RelatedIndex(new FixedIndex(0));
            }
            else
            {
                index = new RelatedIndex(node.index);

                if(node.next != null)
                {
                    node.next.index.index = index;
                    node.next.prev = this;
                }

                prev = node;
                next = node.next;
                node.next = this;
            }

            size++;
        }

        RelatedIndex index;

        KeletonLoadedModule module;

        Node prev;

        Node next;
    }

    interface Index
    {
        int get();
    }

    class RelatedIndex implements Index
    {
        RelatedIndex(Index index)
        {
            this.index = index;
            this.computed = compute();
        }

        int compute()
        {
            this.lastModCount = modCount;
            return index.get() + 1;
        }

        @Override
        public int get()
        {
            if(lastModCount != modCount)
                compute();
            return computed;
        }

        int lastModCount;

        int computed;

        Index index;
    }

    class FixedIndex implements Index
    {
        FixedIndex(int index)
        {
            this.index = index;
        }

        @Override
        public int get()
        {
            return this.index;
        }

        int index;
    }
}
