package com.t4f.lc_helper.utils;

import java.util.HashMap;

public class TrieNode {
    public HashMap<Character, TrieNode> next;
    public boolean isEnd;

    public TrieNode() {
        next = new HashMap<>();
        isEnd = false;
    }
}