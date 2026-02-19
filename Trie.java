import java.util.*;

public class Trie {
	public class Node {
		Map<Character, Node> children;

		long passCount;
		long endCount;

		public Node() {
			children = new HashMap<Character, Node>();
		}

		boolean isEndOfWord() {
			return endCount > 0;
		}

		@Override
		public String toString() {
			return "pass, end: " + passCount + ", " + endCount;
		}
	}

	private Node root;

	public Trie() {
		root = new Node();
	}


	public void insert(String word) {
		if (word == null || word.length() == 0) return;

		Node node = root;

		for (char c : word.toCharArray()) {
			node.passCount++;

			node.children.putIfAbsent(c, new Node());
			node = node.children.get(c);
		}

		node.passCount++;
		node.endCount++;
	}

	public boolean contains(String word) {
		if (word == null || word.length() == 0) return false;

		Node node = root;

		for (char c : word.toCharArray()) {
			if (node.children.containsKey(c)) node = node.children.get(c);
			else return false;
		}

		return node.isEndOfWord();
	}

	public char mostLikelyNextChar(String prefix) {
		if (prefix == null) return '_';

		Node node = root;

		for (char c : prefix.toCharArray()) {
			if (node.children.containsKey(c)) node = node.children.get(c);
			else return '_';
		}

		char top = '_';
		long count = 0;

		for (char c : node.children.keySet()) {
			long passCount = node.children.get(c).passCount;

			if ((c < top && passCount == count) || passCount > count) {
				top = c;
				count = passCount;
			}

		}

		return top;
	}

	public String mostLikelyNextWord(String prefix) {
        if (prefix == null) return "";

        Node node = root;

        for (char c : prefix.toCharArray()) {
            if (node.children.containsKey(c)) {
                node = node.children.get(c);
            } else {
                return "";
            }
        }

        return likeliestSuffix(new StringBuilder(prefix), node);
    }

    private String likeliestSuffix(StringBuilder prefix, Node node) {
        String bestWord = "";
        long maxCount = -1;

        if (node.endCount > 0) {
            bestWord = prefix.toString();
            maxCount = node.endCount;
        }

        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());

            String candidate = likeliestSuffix(prefix, entry.getValue());

            if (!candidate.isEmpty()) {

                Node temp = root;
                for (char c : candidate.toCharArray()) {
                    temp = temp.children.get(c);
                }
                long candidateCount = temp.endCount;

                if (candidateCount > maxCount) {
                    maxCount = candidateCount;
                    bestWord = candidate;
                }
            }

            prefix.setLength(prefix.length() - 1);
        }

        return bestWord;
    }

	public void printWordFrequencies() {

	}

	public static void main(String[] args) {
		Trie trie = new Trie();

		// Space-separated word block (nice & readable)
		String data = """
			apple banana apple apple
			and and and any any
			cat dog dog any any
			apple any banana any
			""";

		// Load words
		for (String w : data.split("\\s+")){ // split on white space
			trie.insert(w.toLowerCase());
		}

		System.out.println("---- contains ----");
		System.out.println("contains(\"apple\") --> " + trie.contains("apple"));
		System.out.println("contains(\"banana\") --> " + trie.contains("banana"));
		System.out.println("contains(\"ban\") --> " + trie.contains("ban"));
		System.out.println("contains(\"zebra\") --> " + trie.contains("zebra"));

		System.out.println("\n---- mostLikelyNextChar ----");
		System.out.println("mostLikelyNextChar(\"a\") --> " + trie.mostLikelyNextChar("a"));
		System.out.println("mostLikelyNextChar(\"ap\") --> " + trie.mostLikelyNextChar("ap"));
		System.out.println("mostLikelyNextChar(\"do\") --> " + trie.mostLikelyNextChar("do"));
		System.out.println("mostLikelyNextChar(\"x\") --> " + trie.mostLikelyNextChar("x"));

		System.out.println("\n---- mostLikelyNextWord ----");
		System.out.println("mostLikelyNextWord(\"a\") --> " + trie.mostLikelyNextWord("a"));
		System.out.println("mostLikelyNextWord(\"ap\") --> " + trie.mostLikelyNextWord("ap"));
		System.out.println("mostLikelyNextWord(\"b\") --> " + trie.mostLikelyNextWord("b"));
		System.out.println("mostLikelyNextWord(\"z\") --> " + trie.mostLikelyNextWord("z"));

		System.out.println("\n---- printWordFrequencies ----");
		trie.printWordFrequencies();
		System.out.println("\nNOTE:  Alphabetical sorting also acceptable\n\n" );

	}
}