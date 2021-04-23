package com.tanmay.autosuggestion;

import java.util.ArrayList;

public class Trie {

    public static final int ALPHABET_SIZE = 26;

    static Trie trie = new Trie();

    private Trie(){};

    TrieNode root = getNode();

    public static Trie getInstance(){
        return trie;
    }

    static class TrieNode
    {
        TrieNode children[] = new TrieNode[ALPHABET_SIZE];
        String number;
        boolean isWordEnd;
    };

    static TrieNode getNode() {
        TrieNode pNode = new TrieNode();
        pNode.isWordEnd = false;
        String number=null;

        for(int i = 0; i < ALPHABET_SIZE; i++)
            pNode.children[i] = null;

        return pNode;
    }

    void insert(final String key,final String number){
        insert(root,key,number);
    }

    static void insert(TrieNode root, final String key,final String pNumber)
    {
        TrieNode pCrawl = root;

        for(int level = 0; level < key.length(); level++)
        {
            int index = (key.charAt(level) - 'a');
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = getNode();
            pCrawl = pCrawl.children[index];
        }
        // mark last node as leaf
        pCrawl.isWordEnd = true;
        pCrawl.number=pNumber;
    }

    static boolean hasNoChild(TrieNode currentNode)
    {
        for(int level=0;level<currentNode.children.length;level++)
        {
            if(currentNode.children[level]!=null)
                return false;

        }

        return true;
    }

    static boolean removeUtil(TrieNode currentNode,String key,
                              int level,int length)
    {
        // If trie is empty
        if(currentNode==null)
            return false;


        // If last character of key is being processed
        if(level==length)
        {

            // This node is no more end of word after
            // removal of given key
            currentNode.isWordEnd=false;

            // If given is not prefix of any other word
            if(hasNoChild(currentNode))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else{

            // If not last character, recur for the child
            // obtained using ASCII value
            TrieNode childNode =
                    currentNode.children[key.charAt(level)-'a'];

            boolean childDeleted =
                    removeUtil(childNode,key,level+1,length);

            if(childDeleted)
            {
                // If root does not have any child
                //(its only child got
                // deleted), and it is not end of another word.
                return (currentNode.isWordEnd
                        &&hasNoChild(currentNode));
            }
        }
        return false;
    }

    void remove(String key)
    {
        int length=key.length();
        if(length>0)
        {
            removeUtil(root,key,0,length);
        }
    }

    static boolean isLastNode(TrieNode root)
    {
        for (int i = 0; i < ALPHABET_SIZE; i++)
            if (root.children[i] != null)
                return false;
        return true;
    }

    static ArrayList<Contact> suggestionsRec(TrieNode root, String currPrefix,ArrayList<Contact> suggestion)
    {
        // found a string in Trie with the given prefix
        if (root.isWordEnd)
        {
            suggestion.add(new Contact(currPrefix,root.number));
        }

        // All children struct node pointers are NULL
        if (isLastNode(root))
            return suggestion;

        for (int i = 0; i < ALPHABET_SIZE; i++)
        {
            if (root.children[i] != null)
            {
                // append current character to currPrefix string
                currPrefix += (char)(97 + i);

                // recur over the rest
                suggestionsRec(root.children[i], currPrefix,suggestion);
            }
        }
        return suggestion;
    }

    static ArrayList<Contact> AutoSuggestions(TrieNode root,
                                              final String query,ArrayList<Contact> suggestion)
    {
        TrieNode pCrawl = root;

        // Check if prefix is present and find the
        // the node (of last level) with last character
        // of given string.
        int level;
        int n = query.length();

        for (level = 0; level < n; level++)
        {
            int index = (query.charAt(level) - 'a');

            // no string in the Trie has this prefix
            if (pCrawl.children[index] == null)
                return suggestion;

            pCrawl = pCrawl.children[index];
        }

        // If prefix is present as a word.
        boolean isWord = (pCrawl.isWordEnd == true);

        // If prefix is last node of tree (has no
        // children)
        boolean isLast = isLastNode(pCrawl);

        // If prefix is present as a word, but
        // there is no subtree below the last
        // matching node.
        if (isWord && isLast)
        {
            suggestion.add(new Contact(query, pCrawl.number));
            return suggestion;
        }

        // If there are are nodes below last
        // matching character.
        if (!isLast)
        {
            String prefix = query;
            suggestionsRec(pCrawl, prefix,suggestion);
            return suggestion;
        }
        return suggestion;
    }

    public ArrayList<Contact> getAutosuggestion(String prefix){
        ArrayList<Contact> suggestion = new ArrayList<>();
        suggestion=AutoSuggestions(root,prefix,suggestion);
        return suggestion;
    }
}
