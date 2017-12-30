package org.kucro3.keleton.implementation.loader;

import com.sun.media.sound.RIFFInvalidDataException;

import java.util.HashMap;
import java.util.Map;

class KeletonLoaderTree {
    KeletonLoaderTree()
    {
    }

    private Map<String, Node> mapped = new HashMap<>();

    private Node head;

    private Node tail;

    private int modCount;

    class Node
    {
        Node(KeletonLoadedModule module)
        {
            this.module = module;
        }

        void remove()
        {
            if(prev != null)
                prev.next = next;

            if(next != null)
            {
                next.prev = prev;
                next.index.index = prev == null ? new RelatedIndex(new FixedIndex(0)) : prev.index;
            }

            mapped.remove(module.getModule().name());

            next = null;
            prev = null;
            module = null;
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
        }

        void linkBefore(Node node)
        {
            if(head == null)
                head = this;

            if(node == null)
            {
                tail = this;
                index = new RelatedIndex(new FixedIndex(0));
            }
            else
            {
                if(node.prev != null)
                {
                    index = new RelatedIndex(node.prev.index);
                    node.prev.next = this;
                }
                else
                    index = new RelatedIndex(new FixedIndex(0));

                node.index.index = index;

                prev = node.prev;
                next = node;
                node.prev = this;
            }
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
