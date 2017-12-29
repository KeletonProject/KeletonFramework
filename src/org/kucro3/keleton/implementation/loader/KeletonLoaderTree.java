package org.kucro3.keleton.implementation.loader;

import java.util.HashMap;
import java.util.Map;

class KeletonLoaderTree {
    KeletonLoaderTree()
    {
    }

    private Map<String, Node> mapped = new HashMap<>();

    private Node head;

    private Node tail;

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
                next.prev = prev;

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
                head = this;
            else
            {
                if(node.next != null)
                    node.next.prev = this;

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
                tail = this;
            else
            {
                if(node.prev != null)
                    node.prev.next = this;

                prev = node.prev;
                next = node;
                node.prev = this;
            }
        }

        KeletonLoadedModule module;

        Node prev;

        Node next;
    }
}
