package com.t4f.lc_helper;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (node.next[c - 'a'] == null)
                node.next[c - 'a'] = new TrieNode();

            node = node.next[c - 'a'];
            if (i == s.length() - 1) node.isEnd = true;
        }
    }

    public boolean search(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (node.next[c - 'a'] == null)
                return false;

            node = node.next[c - 'a'];
        }

        return node.isEnd;
    }

    public void startWith(String s) {

    }

    class TrieNode {
        private TrieNode[] next;
        private boolean isEnd;

        public TrieNode() {
            next = new TrieNode[26];
            isEnd = false;
        }
    }
}
