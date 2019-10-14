package com.t4f.lc_helper.utils;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!node.next.containsKey(c))
                node.next.put(c, new TrieNode());

            node = node.next.get(c);
            if (i == s.length() - 1)
                node.isEnd = true;
        }
    }

    public boolean search(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!node.next.containsKey(c))
                node.next.put(c, new TrieNode());

            node = node.next.get(c);
        }

        return node.isEnd;
    }

    public void startWith(String s) {

    }
}


