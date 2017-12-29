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

        KeletonLoadedModule module;

        Node prev;

        Node next;
    }
}
