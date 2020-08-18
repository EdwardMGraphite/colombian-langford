package digitSequence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColombianRecursion4 {

	private static long[] work;
	private static int[] ns;
	private static int cacheSize;
	private static long cacheHits;
	private static long bigIntHits;
	private static FixedSizeMultiLongLongMap2 memo;
	private static List<BigInteger> bigInts = new ArrayList<>(1000);
	private static long[] powers;
	private static int maxN;
	private static int length;
	private static int keySize;
	private static int leftSentinel;
	private static int rightSentinel;
	
	/**
	 * 
	 * @param args
	 * The first argument should be n, the number of pairs of numbers in each Langford sequence.
	 * The second argument should be the number of records in the cache used to assist the calculation (minimum 1).
	 */
	public static void main(String[] args) {

		System.out.println(Runtime.getRuntime().maxMemory());
		
		maxN = Integer.parseInt(args[0]);
		length = maxN * 2;
		keySize = ((length - 1)/ 64) + 1;
		cacheSize = Integer.parseInt(args[1]);
		
		int n = maxN;
		
		ns = new int[n];
		ns[0] = n - 1;
		ns[n - 1] = n;
		for (int i = 1; i < n - 1; i++)
			ns[i] = i;
		
		powers = new long[64];
		powers[0] = 1;
		for (int i = 1; i < 64; i++)
			powers[i] = 2 * powers[i-1];
		
		work = new long[n+1];
		
		int[] digits = new int[length];
		clearCache();
		System.out.println(new Date());
		BigInteger solutionCount = recurse(digits, 0, new long[(length * 2 + 63) / 64]);
		System.out.println(solutionCount + " solutions");
		long totalWork = 0;
		for (int i=0; i<n+1; i++ ){
			System.out.println(i + " " + work[i]);
			if (i > 0)
				totalWork += work[i];
		}
		
		System.out.println(totalWork + " work");
		System.out.println(memo.size() + " entries in cache");
		System.out.println(bigInts.size() + " BigIntegers");
		
		System.out.println(new Date());
		
	}
	
	private static BigInteger recurse(int[] digits, int index, long[] used){
		
		if (index == maxN){
			//StringBuilder solution = new StringBuilder();
			//for (int i = 0 ; i < digits.length ; i++)
			//	solution.append(digits[i]).append(" ");
			//System.out.println(solution.toString());
			//System.out.println(memo.size() + " entries in cache");
			return BigInteger.ONE;
		}
		
		int n = ns[index];
		
		work[n] = work[n] + 1;
		
		long cacheValue = memo.get(used);
			if (cacheValue > -1) {
				cacheHits++;
				return BigInteger.valueOf(cacheValue);
			}
			else if (cacheValue < -1) {
				cacheHits++;
				bigIntHits++;
				return bigInts.get((int) (-cacheValue - 2));
			}
		
		BigInteger counter = BigInteger.ZERO;
		
		final int maxI = index == 0 ? (maxN + 1) / 2 - 1 :
			index == 1 && leftSentinel * 2 + 1 == maxN ? leftSentinel - 1 :
			length - n - 2;
		
		for (int i = 0; i <= maxI; i++){
			if (digits[i] == 0 && digits[i + n + 1] == 0){
				if (index == 0){
					if (i < Math.ceil(maxN - 1 - Math.sqrt(maxN + 1)) / 2) 
						continue;
					leftSentinel = i;
					rightSentinel = i + n + 1;
					cacheHits = 0;
					bigIntHits = 0;
					clearCache();
				}
				else if (!(i < leftSentinel && i + n + 1 > leftSentinel
							|| i < rightSentinel && i + n + 1 > rightSentinel))
					continue;
				digits[i] = n;
				digits[i + n + 1] = n;
				used[i / 64] += powers[i % 64];
				used[(i + n + 1) / 64] += powers[(i + n + 1) % 64];
				counter = counter.add(recurse(digits, index + 1, used));
				used[i / 64] -= powers[i % 64];
				used[(i + n + 1) / 64] -= powers[(i + n + 1) % 64];
				if (index == 0)
					System.out.println(i + " " + memo.size() + " cache " + cacheHits + " hits " + bigInts.size() + " BigIntegers " + bigIntHits + " bigIntHits " + counter + " solutions " + new Date());
				digits[i] = 0;
				digits[i + n + 1] = 0;
			}
		}
		
		if (memo.isFull()) {
			clearCache();
			System.out.println("Cache cleared");
		}
		if (counter.bitLength() < 64) {
			memo.put(used, counter.longValue());
		}
		else try {
			bigInts.add(counter);
			System.out.println(bigInts.size() + " " + counter.toString());
			memo.put(used, -bigInts.size() - 1);
		}
		catch (Exception e) {
			clearCache();
			System.out.println("Cache cleared");
		}
		
		return counter;
		
	}
	
	private static void clearCache() {
		memo = null;
		bigInts = null;
		System.gc();
		memo = new FixedSizeMultiLongLongMap2(27, cacheSize, keySize);
		bigInts = new ArrayList<>(1000);
	}

}
